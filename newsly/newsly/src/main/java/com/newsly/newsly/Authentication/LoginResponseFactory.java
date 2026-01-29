package com.newsly.newsly.Authentication;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Models.AppUser;
import com.newsly.newsly.TextEditor.Models.JwtToken;
import com.newsly.newsly.TextEditor.Repo.UserRepo;
import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class LoginResponseFactory implements IPipelineStep<LoginRequest,LoginResponse> {

    @SuppressWarnings("unused")
    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;
    private UserRepo repo;

    @Override 
    public LoginResponse execute(LoginRequest request){

        log.info("factory");

        AppUser user= repo.findByUsername(request.getUsername());

        JwtToken accesToken= jwtUtil.generateToken(request.getUsername(),1000*60*15);
        JwtToken refreshToken= jwtUtil.generateToken(request.getUsername(),1000*60*60*24*7, new Long(user.getId()));

        ResponseCookie cookie= cookieUtil.createRefreshCookie(refreshToken.getToken(),Duration.ofMillis(1000*60*60*24*7));

        log.info("ceva");

        return LoginResponse.builder().accesToken(accesToken).refreshToken(refreshToken).refreshCookie(cookie).user(user).build();
        
    }

    
}
