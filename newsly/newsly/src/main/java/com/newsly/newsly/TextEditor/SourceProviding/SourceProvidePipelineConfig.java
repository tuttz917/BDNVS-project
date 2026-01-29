package com.newsly.newsly.TextEditor.SourceProviding;







import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.EmbeddingService;
import com.newsly.newsly.GeneralServices.NliClassification;
import com.newsly.newsly.GeneralServices.TranslateServiceWrapper;
import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;

import com.newsly.newsly.TextEditor.RedisRepo.RedisSourceRepo;
import com.newsly.newsly.TextEditor.Repo.SourceProvideResponseRepo;
import com.newsly.newsly.TextEditor.SourceProviding.Data.RawSourceProvideRequest;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceSimilarityCheckResult;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceContentExtractorStep;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceOpenAiStep;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceProvideRequestFactoryStep;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceProvideResponseLookUpFactoryStep;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceProvideResponseRecallFactoryStep;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceRequestOpenAiResponseParser;
import com.newsly.newsly.TextEditor.SourceProviding.Service.SourceSerpRegistryStep;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.IPipelineStep;
import com.newsly.newsly.library.Pipelines.Pipeline;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@Configuration
public class SourceProvidePipelineConfig{


    private SourceProvideRequestFactoryStep factoryStep;
    private SourceContentTranslateStep translateStep;
    private SourceContentEmbeddingStep embeddingStep;
    private FunctionRegistry<SourceProvideRequest,SourceProvideResponse> resolverRegistry;

    public SourceProvidePipelineConfig(SourceProvideRequestFactoryStep factoryStep
                                        ,SourceContentTranslateStep translateStep
                                        ,SourceContentEmbeddingStep embeddingStep
                                        ,@Qualifier("sourceProvideResolverRegistry")FunctionRegistry<SourceProvideRequest,SourceProvideResponse> resolverRegistry){

                this.factoryStep=factoryStep;
                this.translateStep=translateStep;
                this.embeddingStep=embeddingStep;
                this.resolverRegistry=resolverRegistry;
                                        }



    @Bean("sourceProvidingPipeline")
    public Pipeline<RawSourceProvideRequest,SourceProvideResponse> build(){

    return  Pipeline.<RawSourceProvideRequest>builder()
                                .then(factoryStep)
                                .then(translateStep)
                                .then(embeddingStep)
                                .then(resolverRegistry::applyFirst)
                                .build();
        
    } 



}

@Configuration
class SourceProvideResolverRegistryConfig{

    private Pipeline<SourceProvideRequest,SourceProvideResponse> sourceProvideRecallPipeline;
    private Pipeline<SourceProvideRequest,SourceProvideResponse> lookUpPipeline;

    public SourceProvideResolverRegistryConfig( @Qualifier("sourceProvideRecallPipeline")
    Pipeline<SourceProvideRequest,SourceProvideResponse> sourceProvideRecallPipeline , @Qualifier("sourceProvidingLookUpPipeline") Pipeline<SourceProvideRequest,SourceProvideResponse> lookUpPipeline){


        this.sourceProvideRecallPipeline=sourceProvideRecallPipeline;
        this.lookUpPipeline=lookUpPipeline;

    }

    @Bean("sourceProvideResolverRegistry")
    public FunctionRegistry<SourceProvideRequest,SourceProvideResponse> build(){

        return FunctionRegistry.<SourceProvideRequest,SourceProvideResponse>builder().addFunction(sourceProvideRecallPipeline," ")
                                                                                    .addFunction(lookUpPipeline, " ")
                                                                                    .build()
                                                                                    .applyBy((args)->{return " ";});

    }


}

@Configuration

class SourceProvideLookUpPipelineConfig{

    private SourceSerpRegistryStep sourceRegistryStep;
    private SourceContentExtractorStep contentExtractorStep;
    private SourceOpenAiStep openaiStep;
    private SourceRequestOpenAiResponseParser responseParser;
    private SourceProvideResponseLookUpFactoryStep factoryToResponse;
    private FunctionRegistry<SourceProvideResponse,SourceProvideResponse> persistRegistry;
    private RedisSourceRepo cache;

    public SourceProvideLookUpPipelineConfig(  SourceSerpRegistryStep sourceRegistryStep,
    SourceContentExtractorStep contentExtractorStep,
    SourceOpenAiStep openaiStep,
    SourceRequestOpenAiResponseParser responseParser,
    SourceProvideResponseLookUpFactoryStep factoryToResponse,
    @Qualifier("sourceProvidePersistRegistry")FunctionRegistry<SourceProvideResponse,SourceProvideResponse> persistRegistry,
    RedisSourceRepo cache

    ){

        this.sourceRegistryStep=sourceRegistryStep;
        this.contentExtractorStep=contentExtractorStep;
        this.openaiStep=openaiStep;
        this.responseParser=responseParser;
        this.factoryToResponse=factoryToResponse;
        this.persistRegistry=persistRegistry;
        this.cache=cache;

    }


    @Bean("sourceProvidingLookUpPipeline")
    public Pipeline<SourceProvideRequest,SourceProvideResponse> build(){

    return  Pipeline.<SourceProvideRequest>builder()
                                .then(sourceRegistryStep)
                                .then(contentExtractorStep)
                                .then(openaiStep)
                                .then(responseParser)
                                .then(factoryToResponse)
                                .then(persistRegistry::applyFirst )
                                .thenConsume(cache::save)
                                .build();
        
    } 
    
}


@Configuration
@AllArgsConstructor
class SourceProvidePersistRegistryConfig{

    private RedisSourceRepo cacheRepo;
    private SourceProvideResponseRepo dbRepo;

    @Bean("sourceProvidePersistRegistry")
    public FunctionRegistry<SourceProvideResponse,SourceProvideResponse> build(){

        return FunctionRegistry.<SourceProvideResponse,SourceProvideResponse>builder()
                                            .addConsumer(dbRepo::save," ")
                                            .addConsumer(cacheRepo::saveToQueue, " ")
                                            .build()
                                            .applyBy((args)->{return " ";});

    }

    

}

@Component
@AllArgsConstructor
class SourceContentEmbeddingStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{

    private EmbeddingService service;

    @Override
    public SourceProvideRequest execute(SourceProvideRequest request){

        String content= request.getProcessedContent();

        float[] embedding= service.embeddSentence(content);

        request.setEmbedding(embedding);

        

        return request;

    }


}


@Component
@AllArgsConstructor
class SourceContentTranslateStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{

    private TranslateServiceWrapper service;

    @Override
    public SourceProvideRequest execute(SourceProvideRequest request){

        String content= request.getProcessedContent();

        String translatedContent= this.service.translate(content,"eng");

        request.setProcessedContent(translatedContent);

        return request;

    }

}


@Component
@AllArgsConstructor
class SourceProvidePersistStep implements IPipelineStep<SourceProvideResponse,SourceProvideResponse>{

    private SourceProvideResponseRepo repo;

    @SuppressWarnings("null")
    @Override
    public SourceProvideResponse execute(SourceProvideResponse response){

        SourceProvideResponse savedResponse = repo.save(response);

        return savedResponse;

    }

}

@Component
@AllArgsConstructor
class SourceProvideCacheCheckStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{

    private RedisSourceRepo cache;

    @Override
    public SourceProvideRequest execute(SourceProvideRequest request){

        log.info("ceva");

        SourceSimilarityCheckResult searchResponse= cache.findClosest(request.getEmbedding());

        if (searchResponse.getLinks()== null || searchResponse.getLinks().isEmpty()){
            log.info("Cache Miss");
            throw new RuntimeException();
        }

        request.setSearchResponse(searchResponse);

        return request;

    }

}

@AllArgsConstructor
@Configuration
class SourceProvideRecallPipelineConfig {

    private SourceProvideCacheCheckStep cacheCheckStep;
    private NliClassificationValidationStep nliValidationStep;
    private SourceProvideResponseRecallFactoryStep factoryToResponse;

    @Bean("sourceProvideRecallPipeline")
    public Pipeline<SourceProvideRequest,SourceProvideResponse> build(){

        return Pipeline.<SourceProvideRequest>builder().then(cacheCheckStep).then(nliValidationStep).then(factoryToResponse).build();

    }
    

}

@Component
@AllArgsConstructor
@Slf4j
class NliClassificationValidationStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{


    private NliClassification classification;
   

    @Override
    public SourceProvideRequest execute(SourceProvideRequest request){

        String premise= request.getSearchResponse().getContent();
        String hypotesis= request.getProcessedContent();
        

        String label= classification.apply(premise,hypotesis );

        if(!label.equals("entailment")){
            log.info("nli validation failed");
            throw new RuntimeException();
        }

        return request;

    }


}










