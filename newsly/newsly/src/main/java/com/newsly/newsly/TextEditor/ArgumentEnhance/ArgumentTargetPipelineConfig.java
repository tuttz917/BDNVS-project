package com.newsly.newsly.TextEditor.ArgumentEnhance;




import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceResponseDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Service.ArgumentEnhanceSearchExtractParser;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Service.ArgumentSearchExtracttOpenAiStep;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Service.ArgumentEnhanceContextExtractorStep;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Service.ArgumentEnhanceExtractTargetBoundsStep;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Service.ArgumentEnhanceResponseMapper;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Service.ArgumentEnhanceResponseParser;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.Pipeline;



@Configuration
public class ArgumentTargetPipelineConfig{

    private ArgumentEnhanceExtractTargetBoundsStep targetBoundsExtractor;
    private ArgumentSearchExtracttOpenAiStep searchExtractStep;
    private ArgumentEnhanceSearchExtractParser searchParser;
    private ArgumentEnhanceContextExtractorStep contentExtractStep;
    private FunctionRegistry<ArgumentEnhanceRequest,ArgumentEnhanceRequest> openaiRegistry;
    private ArgumentEnhanceResponseParser responseParser;
    private ArgumentEnhanceResponseMapper mapper;

    
    public ArgumentTargetPipelineConfig(ArgumentSearchExtracttOpenAiStep searchExtractStep, ArgumentEnhanceSearchExtractParser searchParser, ArgumentEnhanceContextExtractorStep contentExtractStep,
                                        @Qualifier("openAiStepRegistry")FunctionRegistry<ArgumentEnhanceRequest,ArgumentEnhanceRequest> openaiStepRegistry, ArgumentEnhanceResponseParser parser,
                                        ArgumentEnhanceResponseMapper mapper, ArgumentEnhanceExtractTargetBoundsStep targetBoundsExtractor){

                                        this.searchExtractStep=searchExtractStep;
                                        this.searchParser=searchParser;
                                        this.openaiRegistry=openaiStepRegistry;
                                        this.responseParser=parser;                                       
                                        this.contentExtractStep=contentExtractStep;
                                        this.mapper=mapper;
                                        this.targetBoundsExtractor=targetBoundsExtractor;

                                        }
    

    @Bean("argumentEnhancePipeline")
    public Pipeline<ArgumentEnhanceRequest,ArgumentEnhanceResponseDto> build(){

        return Pipeline.<ArgumentEnhanceRequest>builder()
                                                        .then(targetBoundsExtractor)
                                                        .then(searchExtractStep)
                                                        .then(searchParser)
                                                        .then(contentExtractStep)
                                                        .then(openaiRegistry::applyFirst)
                                                        .then(responseParser)
                                                        .then(mapper)
                                                        .build();

    }


}



