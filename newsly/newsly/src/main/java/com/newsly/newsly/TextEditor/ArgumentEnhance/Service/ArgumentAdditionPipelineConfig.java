package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentAdditionDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceResponseDto;
import com.newsly.newsly.library.Pipelines.Pipeline;

@Configuration
public class ArgumentAdditionPipelineConfig{

    private ArgumentEnhanceRequestFactory factory;
    private Pipeline<ArgumentEnhanceRequest,ArgumentEnhanceResponseDto> argumentEnhancePipeline;

    public ArgumentAdditionPipelineConfig(ArgumentEnhanceRequestFactory factory, @Qualifier("argumentEnhancePipeline") Pipeline<ArgumentEnhanceRequest,ArgumentEnhanceResponseDto> argumentEnhancePipeline ){

        this.factory=factory;
        this.argumentEnhancePipeline=argumentEnhancePipeline;

    }

    @Bean("argumentAdditionPipeline")
    public Pipeline<ArgumentAdditionDto,ArgumentEnhanceResponseDto> build(){

        return Pipeline.<ArgumentAdditionDto>builder().then(factory::mapFromAdditionRequest).then(argumentEnhancePipeline).build();

    }

}