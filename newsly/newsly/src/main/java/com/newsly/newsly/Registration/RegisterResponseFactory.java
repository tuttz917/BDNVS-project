package com.newsly.newsly.Registration;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.newsly.newsly.Authentication.CookieUtil;
import com.newsly.newsly.Authentication.JwtUtil;
import com.newsly.newsly.TextEditor.Models.AppUser;
import com.newsly.newsly.TextEditor.Models.JwtToken;
import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;







@AllArgsConstructor
@Component
public class RegisterResponseFactory  implements IPipelineStep<RegisterRequest,RegisterResponse>{

    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;

    @Override
    public RegisterResponse execute(RegisterRequest request){

        log.info("register resp factory");

        AppUser appUser= AppUserFactory.getUserFromRegistration(request);



        JwtToken accesToken= jwtUtil.generateToken(appUser.getUsername(),1000*60*15);
        JwtToken refreshToken= jwtUtil.generateToken(appUser.getUsername(), 1000*60*60*24*7);

        log.info("ceva");

        ResponseCookie cookie= cookieUtil.createRefreshCookie(refreshToken.getToken(),Duration.ofDays(7));



        return  RegisterResponse.builder()
                            .appUser(appUser)
                            .accesToken(accesToken)
                            .refreshToken(refreshToken)
                            .refreshCookie(cookie)
                            .build();

    }

}


