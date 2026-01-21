package com.newsly.newsly.Authentication;

import org.springframework.stereotype.Component;

import com.newsly.newsly.library.Pipelines.IPipelineStep;


@Component
public class LoginResponseInitializer implements IPipelineStep<LoginRequest,LoginResponse> {


    @Override 
    public LoginResponse execute(LoginRequest request){

        
        // context.put("loginRequest", request);

        // return LoginResponse.builder()
        //         .build();

        return null;

    }
    
}
