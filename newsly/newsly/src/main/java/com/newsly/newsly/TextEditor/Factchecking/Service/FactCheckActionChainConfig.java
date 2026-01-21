package com.newsly.newsly.TextEditor.Factchecking.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;

import com.newsly.newsly.TextEditor.Models.FactCheckResponse;

import com.newsly.newsly.library.Pipelines.ActionChain;
import com.newsly.newsly.library.Pipelines.Pipeline;




@Configuration
public class FactCheckActionChainConfig {

    Pipeline<FactCheckRequest,FactCheckResponse> persistPipeline;
    
    public FactCheckActionChainConfig(@Qualifier("persistPipeline") Pipeline<FactCheckRequest,FactCheckResponse> persistPipeline){

        this.persistPipeline=persistPipeline;

    }

    @Bean("factCheckActionChain")
    public ActionChain<FactCheckRequest> factCheckActionChain(){

        return ActionChain.<FactCheckRequest>builder()
                                        .add(persistPipeline)
                                        .build();

    }

    
}
