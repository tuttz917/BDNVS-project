package com.newsly.newsly.Authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.library.Validators.ValidationChain;

@Configuration
public class LoginValidationChainConfig {


    @Qualifier("loginValidationChain")
    @Bean
    ValidationChain<LoginRequest> LoginValidationChain(UsernamePredicate usernamePredicate, PasswordPredicate passwordPredicate){

        return ValidationChain.<LoginRequest>builder().then(usernamePredicate)
                                            .then(passwordPredicate)                               
                                            .build();

    }

}
