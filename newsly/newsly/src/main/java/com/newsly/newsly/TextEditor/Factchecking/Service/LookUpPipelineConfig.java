package com.newsly.newsly.TextEditor.Factchecking.Service;




import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;

import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.ContentExtractorStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckAiResponseParser;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckRequestToFactCheckResponseMapper;

import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.OpenaiFactCheckStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.SerpRegistryStep;
import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.TextEditor.RedisRepo.RedisFactCheckRepo;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.Pipeline;




@Configuration
public class LookUpPipelineConfig {

    private SerpRegistryStep serpRegistryStep;
    private ContentExtractorStep contentExtractorStep;
    private OpenaiFactCheckStep openaiFactCheckStep;
    private FactCheckAiResponseParser responseParser;
    private FactCheckRequestToFactCheckResponseMapper factoryMapper;
    private FunctionRegistry<FactCheckResponse,FactCheckResponse> factCheckPersistFunctionRegistry;
    private RedisFactCheckRepo cache;

            public LookUpPipelineConfig(    SerpRegistryStep serpRegistryStep,
                                                ContentExtractorStep contentExtractorStep,
                                                OpenaiFactCheckStep openaiFactCheckStep,
                                                FactCheckAiResponseParser responseParser,
                                                FactCheckRequestToFactCheckResponseMapper factoryMapper,
                                                @Qualifier("factCheckPersistFunctionRegistry")FunctionRegistry<FactCheckResponse,FactCheckResponse> factCheckPersistFunctionRegistry,
                                                RedisFactCheckRepo cache)
                                                {

                                                    this.serpRegistryStep=serpRegistryStep;
                                                    this.contentExtractorStep=contentExtractorStep;
                                                    this.openaiFactCheckStep=openaiFactCheckStep;
                                                    this.responseParser=responseParser;
                                                    this.factoryMapper=factoryMapper;
                                                    this.factCheckPersistFunctionRegistry=factCheckPersistFunctionRegistry;
                                                    this.cache=cache;

                            
                                                }

    @Bean(name="lookUpPipeline")
    public Pipeline<FactCheckRequest,FactCheckResponse> lookUpPipeline(){

        return  Pipeline.<FactCheckRequest>builder().then(serpRegistryStep)
                                                        .then(contentExtractorStep)
                                                        .then(openaiFactCheckStep)
                                                        .then(responseParser)
                                                        .then(factoryMapper)
                                                        .then(factCheckPersistFunctionRegistry::applyFirst)
                                                        .thenConsume(cache::save)
                                                        .build();

            

    }


}
