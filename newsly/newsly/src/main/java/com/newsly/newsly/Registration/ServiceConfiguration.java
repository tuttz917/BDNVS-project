package com.newsly.newsly.Registration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Repo.JwtTokenRepo;
import com.newsly.newsly.TextEditor.Repo.UserRepo;
import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.Pipeline;
import com.newsly.newsly.library.Validators.ValidationChain;


@Configuration
public class ServiceConfiguration {


    @Bean("registrationPipeline")
    Pipeline<RegisterRequest, RegisterResponse> RegistrationPipeline(
        ValidationChain<RegisterRequest> regReqValidationChain,
        RegisterResponseFactory responseGenerator,
        @Qualifier("registerResponseActionChain")ActionChain<RegisterResponse> registerResponseActionChain) {


    
    return  Pipeline.<RegisterRequest>builder()

                    .then(regReqValidationChain)
                    .then(responseGenerator)
                    .thenConsume(registerResponseActionChain::runTransactionalChain)
                    .build();

}


}


@Configuration 
class RegisterResponseActionChainConfig{


    @Bean("registerResponseActionChain")
    public ActionChain<RegisterResponse> build(UserRepo userRepo,JwtTokenRepo jwtRepo){

        return ActionChain.<RegisterResponse>builder().add((resp)->{userRepo.save(resp.getAppUser());})
                                                    .add((resp)->{jwtRepo.save(resp.getRefreshToken());})
                                                    .transactionManager("editorTm")
                                                    .build();

    }

}