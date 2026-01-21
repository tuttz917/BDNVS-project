package com.newsly.newsly.Authentication;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class LoginRefreshCookieFactory implements IPipelineStep<LoginResponse,LoginResponse> {

    CookieUtil cookieUtil;

    @Override 
    public LoginResponse execute(LoginResponse loginResponse){


        String refreshTokenText= loginResponse.getRefreshToken().getToken();

        ResponseCookie cookie= cookieUtil.createRefreshCookie(refreshTokenText,Duration.ofDays(7)); 

        loginResponse.setRefreshCookie(cookie); 

        return loginResponse;

    }
    
}
