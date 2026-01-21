package com.newsly.newsly.Authentication;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.AppRole;
import com.newsly.newsly.Registration.Repository.AppRoleRepo;
import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component 
public class RoleIdentifier implements IPipelineStep<LoginResponse,LoginResponse> {

    AppRoleRepo roleRepo;

    @Override 
    public LoginResponse execute(LoginResponse loginResponse) {


        AppRole role= roleRepo.findByRole(loginResponse.getRole().getRole());

        loginResponse.setRole(role);

        return loginResponse;

    }

    
}
