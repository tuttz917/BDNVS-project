package com.newsly.newsly.Authentication;

import org.springframework.stereotype.Component;


import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class LoginTokenFactory implements IPipelineStep<LoginResponse,LoginResponse> {

    @SuppressWarnings("unused")
    private JwtUtil jwtUtil;

    @Override 
    public LoginResponse execute(LoginResponse loginResponse){

        // LoginRequest loginRequest= context.get("loginRequest",LoginRequest.class);

        // JwtToken accesToken= jwtUtil.generateToken(loginRequest.getUsername(),1000*60*15, loginResponse.getRole().getRole());
        // JwtToken refreshToken= jwtUtil.generateToken(loginRequest.getUsername(),1000*60*60*24*7, loginResponse.getRole().getRole());

        // loginResponse.setAccesToken(accesToken);
        // loginResponse.setRefreshToken(refreshToken);


        // return loginResponse;
        return null;
    }

    
}
