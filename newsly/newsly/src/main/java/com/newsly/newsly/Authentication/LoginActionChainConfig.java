package com.newsly.newsly.Authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.Pipeline;

@Configuration
public class LoginActionChainConfig {


    @Qualifier("loginActionChain")
    @Bean
    ActionChain<LoginResponse> LoginActionChain(AuthenticationManager authManager, Pipeline<LoginResponse, LoginResponse> refreshTokenPersister){


        return ActionChain.<LoginResponse>builder()
                        .add(refreshTokenPersister)
                        .add(authManager)
                        .build();

    }


    @Qualifier("refreshTokenPersister")
    @Bean
    Pipeline<LoginResponse, LoginResponse> refreshTokenPersister(RefreshTokenEncoder encoder, JwtTokenDbPersister persister){


        return Pipeline.<LoginResponse>builder()
                            .then(encoder)
                            .thenConsume(persister)
                            .build();


    }


}
