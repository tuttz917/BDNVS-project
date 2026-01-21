package com.newsly.newsly.Registration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.Pipeline;
import com.newsly.newsly.library.Validators.ValidationChain;


@Configuration
public class ServiceConfiguration {

  
    @Bean("registrationPipeline")
    Pipeline<RegisterRequestInputDto, RegisterResponse> RegistrationPipeline(
        ValidationChain<RegisterRequestInputDto> regReqValidationChain,
        RegisterResponseGenerator responseGenerator,
        ActionChain<RegisterResponse> registerResponseActionChain) {


    
        return  Pipeline.<RegisterRequestInputDto>builder()

                    .then(regReqValidationChain)
                    .then(responseGenerator)
                    .thenConsume(registerResponseActionChain::runTransactionalChain)
                    .build();

}


}
