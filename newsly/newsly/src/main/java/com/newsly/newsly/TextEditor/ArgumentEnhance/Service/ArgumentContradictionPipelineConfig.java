package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentContradictDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceResponseDto;
import com.newsly.newsly.library.Pipelines.Pipeline;

@Configuration
public class ArgumentContradictionPipelineConfig{

    private ArgumentEnhanceRequestFactory factory;
    private Pipeline<ArgumentEnhanceRequest,ArgumentEnhanceResponseDto> argumentEnhancePipeline;

    public ArgumentContradictionPipelineConfig(ArgumentEnhanceRequestFactory factory, @Qualifier("argumentEnhancePipeline") Pipeline<ArgumentEnhanceRequest,ArgumentEnhanceResponseDto> argumentEnhancePipeline ){

        this.factory=factory;
        this.argumentEnhancePipeline=argumentEnhancePipeline;

    }

    @Bean("argumentContradictionPipeline")
    public Pipeline<ArgumentContradictDto,ArgumentEnhanceResponseDto> build(){

        return Pipeline.<ArgumentContradictDto>builder().then(factory::mapFromContradictionRequest).then(argumentEnhancePipeline).build();

    }

}