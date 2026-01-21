package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class OpenAiStepRegistryConfig{

    private ArgumentEnhanceOpenAiStep argumentEnhanceStep;
    private ArgumentContradictOpenAiStep argumentContradictStep;
    

    @Bean("openAiStepRegistry")
    public FunctionRegistry<ArgumentEnhanceRequest,ArgumentEnhanceRequest> build(){

        return FunctionRegistry.<ArgumentEnhanceRequest,ArgumentEnhanceRequest>builder().addFunction(argumentContradictStep,"contradict")
                                                                                        .addFunction(argumentEnhanceStep, "addition")
                                                                                        .build()
                                                                                        .applyBy((input)->{return input.getType();});

            
    }

}