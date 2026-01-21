package com.newsly.newsly.TextEditor.Factchecking.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckResponseDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.RawFactCheckRequest;

import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.EmbeddingStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckRequestFactoryStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckResponseDtoMapper;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.TextNormalizerStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.ToxicityValidatorStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.TranslateClientStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.WordCountStep;
import com.newsly.newsly.TextEditor.Models.FactCheckResponse;

import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.Pipeline;



@Configuration
public class FactCheckPipelineConfig {

    private WordCountStep wordCountStep;
    private FactCheckRequestFactoryStep factoryStep;
    private TextNormalizerStep normalizerStep;
    private TranslateClientStep translateStep;
    private ToxicityValidatorStep toxicityValidatorStep;
    private EmbeddingStep embeddingStep;
    private FunctionRegistry<FactCheckRequest,FactCheckResponse> factCheckResolver;
    private FactCheckResponseDtoMapper mapper;


    public FactCheckPipelineConfig( WordCountStep wordCountStep
                            , FactCheckRequestFactoryStep factoryStep
                            , TextNormalizerStep normalizerStep
                            , TranslateClientStep translateStep
                            , ToxicityValidatorStep toxicityValidatorStep
                            , EmbeddingStep embeddingStep
                            , @Qualifier("factCheckResolver") FunctionRegistry<FactCheckRequest,FactCheckResponse> factCheckResolver
                            , FactCheckResponseDtoMapper mapper
                        
                        ){


        this.wordCountStep=wordCountStep;
        this.factoryStep=factoryStep;
        this.normalizerStep=normalizerStep;
        this.translateStep=translateStep;
        this.toxicityValidatorStep=toxicityValidatorStep;
        this.embeddingStep=embeddingStep;
        this.factCheckResolver=factCheckResolver;
        this.mapper=mapper;

    }

    

    @Bean("factCheckPipeline")
    public Pipeline<RawFactCheckRequest,FactCheckResponseDto> factCheckPipeline(){
            

            return Pipeline.<RawFactCheckRequest>builder()
                                                .then(wordCountStep)
                                                .then(factoryStep)
                                                .then(normalizerStep)
                                                .then(translateStep)
                                                .then(toxicityValidatorStep)
                                                .then(embeddingStep)
                                                .then(factCheckResolver::applyFirst)
                                                .then(mapper)
                                                .build();
                                                            
    }

}
