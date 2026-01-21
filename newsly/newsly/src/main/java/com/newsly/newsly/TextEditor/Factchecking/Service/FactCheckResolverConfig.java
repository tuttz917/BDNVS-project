package com.newsly.newsly.TextEditor.Factchecking.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;

import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.Pipeline;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class FactCheckResolverConfig {

    @Qualifier("lookUpPipeline")
    private Pipeline<FactCheckRequest,FactCheckResponse> lookUpPipeline;
    @Qualifier("extractSimilarPipeline")
    private Pipeline<FactCheckRequest,FactCheckResponse> extractSimilarPipeline;
    
    @Bean("factCheckResolver")
    public FunctionRegistry<FactCheckRequest,FactCheckResponse> factCheckResolver(){

            return FunctionRegistry.<FactCheckRequest,FactCheckResponse>builder()
                                            .addFunction(extractSimilarPipeline, " ")
                                            .addFunction(lookUpPipeline," ")
                                            .build()
                                            .applyBy((data)->{return " ";});

    }


}
