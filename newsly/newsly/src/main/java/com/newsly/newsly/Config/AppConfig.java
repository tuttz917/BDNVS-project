package com.newsly.newsly.Config;


import java.util.concurrent.Executor;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import org.springframework.web.reactive.function.client.WebClient;

import com.newsly.newsly.Authentication.EncryptionService;

import com.newsly.newsly.Authentication.JwtUtil;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
public class AppConfig {

 

  @Bean PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean 
  JwtUtil jwtUtil(){
    return new JwtUtil("cheieSecreta2");
  }

 @Bean
 EncryptionService encryptionService(){
  return new EncryptionService("cheieSecreta");
 }

 @Bean
WebClient webClient(WebClient.Builder builder) {
    return builder
        .codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(10 * 1024 * 1024))
        .build();
}


  @Bean("articleExecutor")
  Executor articleExecutor(){
      ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(16);
      executor.setMaxPoolSize(42);
      executor.initialize();
      return executor;
  }

  @Bean("feedExecutor")
  Executor feedExecutor(){
    ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(12);
    executor.initialize();
    return executor;
  }

  @Bean("taskExecutor")
  Executor taskExecutor(){
    ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(12);
    executor.initialize();
    return executor;
  }


  }






