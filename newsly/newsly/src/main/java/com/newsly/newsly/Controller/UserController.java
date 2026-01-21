package com.newsly.newsly.Controller;



import java.util.Map;



import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;



import org.springframework.web.bind.annotation.RestController;

import com.newsly.newsly.Authentication.CookieUtil;

import com.newsly.newsly.Authentication.LoginRequest;
import com.newsly.newsly.Authentication.LoginResponse;
import com.newsly.newsly.Registration.RegisterRequestInputDto;
import com.newsly.newsly.Registration.RegisterResponse;
import com.newsly.newsly.library.Pipelines.Pipeline;

import com.newsly.newsly.library.Pipelines.Result;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;






@AllArgsConstructor
@RestController
public class UserController {

    Pipeline<LoginRequest,LoginResponse> loginPipeline;
    CookieUtil cookieUtil;
    Pipeline<RegisterRequestInputDto,RegisterResponse> registerPipeline;


    

    @PostMapping("/api/auth")
    public  ResponseEntity<Map<String,String>> handleLoginRequest(@RequestBody LoginRequest loginRequest) {
    

        Result<LoginRequest,LoginResponse> result=loginPipeline.run(loginRequest);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, result.getResultData()
                                                .getRefreshCookie()
                                                .toString()  )
            .body(Map.of(
                        "acces_token",result.getResultData()
                                            .getAccesToken()
                                            .getToken()
            ));
            

    }

        @PostMapping("/api/register")
        public ResponseEntity<Map<String,String>> registerUser(@RequestBody RegisterRequestInputDto request) {
            

            return null;
        }
        
    
    

    
}







