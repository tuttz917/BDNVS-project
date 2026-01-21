package com.newsly.newsly.TextEditor.Factchecking.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckRequestToFactCheckResponseMapper;
import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.TextEditor.RedisRepo.RedisFactCheckRepo;
import com.newsly.newsly.TextEditor.Repo.FactCheckResponseRepo;
import com.newsly.newsly.library.Pipelines.IPipelineStep;
import com.newsly.newsly.library.Pipelines.Pipeline;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class PersistPipelineConfig {

    private FactCheckRequestToFactCheckResponseMapper mapper; 
    private PersistFactCheckResponse persistenceStep;
    private RedisFactCheckRepo cacheStep;


    @Bean("persistPipeline")
    public Pipeline<FactCheckRequest,FactCheckResponse> build(){

        return Pipeline.<FactCheckRequest>builder().then(mapper).then(persistenceStep).thenConsume(cacheStep::save).build();
        
    }


    
}

@AllArgsConstructor
@Component
class PersistFactCheckResponse implements IPipelineStep<FactCheckResponse,FactCheckResponse>{

    FactCheckResponseRepo repo;

    @Override
    public FactCheckResponse execute(FactCheckResponse response){

        log.info("persistam in bd");

        log.info(response.toString());

        FactCheckResponse savedResponse= repo.save(response);  

        log.info("operatiunea a functionat");

        return savedResponse;

    }

}


