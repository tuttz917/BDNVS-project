package com.newsly.newsly.TextEditor.Factchecking.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.TextEditor.RedisRepo.RedisFactCheckRepo;
import com.newsly.newsly.TextEditor.Repo.FactCheckResponseRepo;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class FactCheckPersistFunctionRegistryConfig {
    

    private FactCheckResponseRepo dbRepo;
    private RedisFactCheckRepo cacheRepo;

    @Bean("factCheckPersistFunctionRegistry")
    public FunctionRegistry<FactCheckResponse,FactCheckResponse> build(){

        return FunctionRegistry.<FactCheckResponse,FactCheckResponse>builder().addConsumer(dbRepo::save, " ")
                                                                            .addConsumer(cacheRepo::saveInQueue," ")
                                                                            .build()
                                                                            .applyBy((arg)->{return " ";});

    }

}
