package com.newsly.newsly.TextEditor.Factchecking.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckSimilarityCheckResult;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckLabelResolverStep;
import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.FactCheckRequestToFactCheckResponseMapper;

import com.newsly.newsly.TextEditor.Factchecking.Service.Steps.NliClassificationStep;

import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.TextEditor.RedisRepo.RedisFactCheckRepo;

import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.IPipelineStep;
import com.newsly.newsly.library.Pipelines.Pipeline;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Configuration
public class ExtractSimilarResponsePipelineConfig {

    private FunctionRegistry<FactCheckRequest,FactCheckRequest> extractSimilarResponseRegistry;
    private NliClassificationStep nliClassification;
    private FactCheckLabelResolverStep labelResolverStep;
    private FactCheckRequestToFactCheckResponseMapper mapper;

    public ExtractSimilarResponsePipelineConfig(NliClassificationStep nliClassificationStep
                                                ,FactCheckLabelResolverStep labelResolverStep
                                                ,@Qualifier("extractSimilarResponseRegistry") FunctionRegistry<FactCheckRequest,FactCheckRequest> extractSimilarResponseRegistry
                                                ,FactCheckRequestToFactCheckResponseMapper mapper){


            this.nliClassification=nliClassificationStep;
            this.labelResolverStep=labelResolverStep;
            this.extractSimilarResponseRegistry=extractSimilarResponseRegistry;
            this.mapper=mapper;

    }

    @Bean("extractSimilarPipeline")
    public Pipeline<FactCheckRequest,FactCheckResponse> extractSimilarPipeline(){

        return Pipeline.<FactCheckRequest>builder().then(extractSimilarResponseRegistry::applyFirst)
                                                .then(nliClassification)
                                                .then(labelResolverStep)
                                                .then(mapper)
                                                .build();
                                                

    }

    
}


@Configuration
@AllArgsConstructor
class ExtractSimilarResponseRegistryConfig{

    private ExtractSimilarResponseCacheStep cacheStep;

    @Bean("extractSimilarResponseRegistry")
    public FunctionRegistry<FactCheckRequest,FactCheckRequest> build(){

        return FunctionRegistry.<FactCheckRequest,FactCheckRequest>builder().addFunction(cacheStep," ").build().applyBy((args)->{return " ";});

    }


}



@Slf4j
@Component
@AllArgsConstructor
class ExtractSimilarResponseCacheStep implements IPipelineStep<FactCheckRequest,FactCheckRequest>{

    private RedisFactCheckRepo repo;

    @Override
    public FactCheckRequest execute(FactCheckRequest request){

        FactCheckSimilarityCheckResult response= repo.findClosest(request.getEmbedding());

        if (response!= null){
            log.info("cache hit");
            log.info(response.toString());
        }

        request.setSimilarityCheckResult(response);

        return request;

    }


}



