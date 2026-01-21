package com.newsly.newsly.Registration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.ConsumerRegistry;

@Configuration
public class RegisterResponseChainConfiguration {

    @Qualifier("registerResponseActionChain")
    @Bean
    ActionChain<RegisterResponse> RegisterResponseActionChain(
                                                                UserRepoPersister userPersister, JwtTokenPersister jwtPersister,
                                                                ConsumerRegistry<RegisterResponse> profilePersisterRegistry){

            return ActionChain.<RegisterResponse>builder().add(userPersister)
                                                            .add(profilePersisterRegistry)
                                                            .add(jwtPersister)
                                                            .build();
                                                            
                                            

    }

    @Qualifier("profilePersisterRegistry")
    @Bean
    ConsumerRegistry<RegisterResponse> ProfilePersisterRegistry(ClientProfilePersister clientPersister){

            return ConsumerRegistry.<RegisterResponse>builder().addConsumer(clientPersister, "client").build();

    }

    

}
