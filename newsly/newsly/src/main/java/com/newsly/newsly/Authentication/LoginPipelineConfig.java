package com.newsly.newsly.Authentication;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Repo.JwtTokenRepo;
import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.Pipeline;
import com.newsly.newsly.library.Validators.ValidationChain;

import lombok.extern.slf4j.Slf4j;


@Configuration 
public class LoginPipelineConfig {

   
    @Bean("loginPipeline")
    public Pipeline<LoginRequest, LoginResponse> LoginPipeline(
                                        ValidationChain<LoginRequest> loginValidationChain
                                        , LoginResponseFactory responseFactory
                                        , @Qualifier("loginPipelineActionChain") ActionChain<LoginResponse> loginActionChain  ) {

        
        return Pipeline.<LoginRequest>builder()
                                            .then(loginValidationChain)
                                            .then(responseFactory)
                                            .thenConsume(loginActionChain::runTransactionalChain)
                                            .build();
        


}
    

}
@Slf4j
@Configuration
class LoginResponseActionChain{


    @Bean("loginPipelineActionChain")
    public ActionChain<LoginResponse> actionChain(JwtTokenRepo tokenRepo){

        return ActionChain.<LoginResponse>builder().add(

                    (resp)->{

                        log.info(resp.getUser().toString());
                        try{
                            
                        tokenRepo.deleteByUserId(resp.getUser().getId());
                        }catch(Exception e){
                            log.info("eroare aici");
                            log.info(e.getMessage());
                        }

                        tokenRepo.save(resp.getRefreshToken());

                    }

        ).transactionManager("editorTm").build();

    }

}