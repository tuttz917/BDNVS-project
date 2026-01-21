package com.newsly.newsly.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.JwtToken;
import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class RefreshTokenEncoder implements IPipelineStep<LoginResponse,LoginResponse>{

    private final PasswordEncoder encoder;

    @Override
    public LoginResponse execute(LoginResponse loginRequest){

        JwtToken refreshToken= loginRequest.getRefreshToken();

        String encodedRefreshTokenString= encoder.encode(refreshToken.getToken());

        refreshToken.setToken(encodedRefreshTokenString);

        return loginRequest;


    }
    

}

