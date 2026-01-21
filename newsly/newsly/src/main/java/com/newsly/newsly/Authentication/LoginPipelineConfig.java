package com.newsly.newsly.Authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.Pipeline;
import com.newsly.newsly.library.Validators.ValidationChain;


@Configuration 
public class LoginPipelineConfig {

    @Qualifier("loginPipeline")
    @Bean
    Pipeline<LoginRequest, LoginResponse> LoginPipeline(
                                        ValidationChain<LoginRequest> loginValidationChain
                                        , LoginResponseInitializer initializer
                                        , RoleIdentifier roleIdentifier
                                        , LoginTokenFactory tokenFactory
                                        , LoginRefreshCookieFactory cookieFactory
                                        , RefreshTokenEncoder refreshTokenEncoder
                                        , ActionChain<LoginResponse> loginActionChain) {

        
        return Pipeline.<LoginRequest>builder()
                                            .then(loginValidationChain)
                                            .then(initializer)
                                            .then(roleIdentifier)
                                            .then(tokenFactory)
                                            .then(cookieFactory)
                                            .thenConsume(loginActionChain::runChain)
                                            .build();
        


}
    

}