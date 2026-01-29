package com.newsly.newsly.Controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.newsly.newsly.Authentication.CookieUtil;
import com.newsly.newsly.Authentication.JwtUtil;
import com.newsly.newsly.Authentication.LoginRequest;
import com.newsly.newsly.Authentication.LoginResponse;
import com.newsly.newsly.Registration.RegisterRequest;
import com.newsly.newsly.Registration.RegisterResponse;
import com.newsly.newsly.TextEditor.Models.JwtToken;
import com.newsly.newsly.TextEditor.Repo.JwtTokenRepo;
import com.newsly.newsly.TextEditor.Repo.UserRepo;
import com.newsly.newsly.library.Pipelines.Pipeline;

import com.newsly.newsly.library.Pipelines.Result;
import com.openai.models.beta.realtime.ResponseCreateEvent.Response;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {

    Pipeline<LoginRequest, LoginResponse> loginPipeline;
    CookieUtil cookieUtil;
    Pipeline<RegisterRequest, RegisterResponse> registerPipeline;
    JwtTokenRepo tokenRepo;
    UserRepo userRepo;
    JwtUtil jwtUtil;

    @PostMapping("/api/v1/auth")
    public ResponseEntity<Map<String, String>> handleLoginRequest(@RequestBody LoginRequest loginRequest) {

        Result<LoginRequest, LoginResponse> result = loginPipeline.run(loginRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, result.getResultData()
                        .getRefreshCookie()
                        .toString())
                .body(Map.of(
                        "acces_token", result.getResultData()
                                .getAccesToken()
                                .getToken()));

    }

    @PostMapping("/api/v1/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {

        log.info("am primit cererea");

        RegisterResponse resp= registerPipeline.run(request).getResultData();

        log.info(resp.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resp.getRefreshCookie().toString())
                .body(Map.of("acces_token", resp.getAccesToken().getToken()));

    
                
    }

    @PostMapping("/api/v1/refresh")
    public ResponseEntity<Map<String,String>> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken ) {

        log.info("refresh");

        if (refreshToken==null || refreshToken.isBlank()){

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        try{

                JwtToken token= JwtToken.builder().token(refreshToken).build();

                String username= jwtUtil.extractUsername(token);
                
                log.info(username);
                log.info(refreshToken);
                
                Long id= userRepo.findByUsername(username).getId();
                log.info(Long.toString(id));
                JwtToken dbToken= tokenRepo.findByToken(refreshToken);

                if (dbToken == null) {
                        log.error("Token-ul primit din cookie NU există în baza de date!");
                        log.error("Token primit: " + refreshToken);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}

                log.info(Long.toString(dbToken.getUserId()));

                if(id!= dbToken.getUserId() ){

                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

                }

                        try{
                                
                                JwtToken accesToken= jwtUtil.generateToken(username, 1000*60*30);

                        return ResponseEntity.ok().body(Map.of(
                                "acces_token", accesToken.getToken()

                        ));
                        }catch(Exception e){
                                log.info("token invalid");
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                        }

                

        }catch(Exception e){

                log.info("eroare la creare/verificare token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        
        
}


}
