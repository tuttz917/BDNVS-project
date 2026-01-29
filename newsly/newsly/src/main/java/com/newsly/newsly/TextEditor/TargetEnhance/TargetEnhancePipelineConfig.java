package com.newsly.newsly.TextEditor.TargetEnhance;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.GeneralServices.OpenAiService;
import com.newsly.newsly.TextEditor.TargetEnhance.Data.TargetEnhanceRawRequest;
import com.newsly.newsly.TextEditor.TargetEnhance.Data.TargetEnhanceRequest;
import com.newsly.newsly.TextEditor.TargetEnhance.Data.TargetEnhanceResponse;
import com.newsly.newsly.library.Pipelines.IPipelineStep;
import com.newsly.newsly.library.Pipelines.Pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;




@Builder
@Data
class TargetEnhanceRequestDto{

    private String context;
    private String target;

}



@AllArgsConstructor
@Configuration
public class TargetEnhancePipelineConfig{

    private TargetEnhanceRequestFactoryStep factoryStep;
    private TargetEnhanceOpenAiStep openAiStep;
    private TargetEnhanceRequestParserStep parser;
    private TargetEnhanceResponseFactoryStep factoryToResponseStep;
    


    @Bean("targetEnhancePipeline")
    public Pipeline<TargetEnhanceRawRequest, TargetEnhanceResponse> build(){

        return Pipeline.<TargetEnhanceRawRequest>builder()
                                                .then(factoryStep)
                                                .then(openAiStep)
                                                .then(parser)
                                                .then(factoryToResponseStep)
                                                .build();

    } 

    

}

@Configuration
@Slf4j
@Component
class TargetEnhanceRequestFactoryStep implements IPipelineStep<TargetEnhanceRawRequest, TargetEnhanceRequest>{

    @Override
    public TargetEnhanceRequest execute(TargetEnhanceRawRequest rawRequest){

        log.info("factory step");

        String target= rawRequest.getTarget();
        String context= rawRequest.getContext();

        return TargetEnhanceRequest.builder().context(context).target(target).build();

    }

}


@Component
class TargetEnhanceOpenAiStep implements IPipelineStep<TargetEnhanceRequest,TargetEnhanceRequest>{

    private OpenAiService aiService;
    private String prompt;

    public TargetEnhanceOpenAiStep(OpenAiService service, @Qualifier("target-semantic-enhance prompt") String prompt ){

        this.aiService=service;
        this.prompt=prompt;

    }

    @Override 
    public TargetEnhanceRequest execute(TargetEnhanceRequest request){

        log.info("openai step");

        String context= request.getContext();
        String Target= request.getTarget();

        TargetEnhanceRequestDto dto= TargetEnhanceRequestDto.builder().context(context).target(Target).build();

        JsonNode node= aiService.applyCompletion(prompt, dto);

        request.setResponseNode(node);

        log.info(node.toString());

        return request;

    }

}


@Component
class TargetEnhanceRequestParserStep implements IPipelineStep<TargetEnhanceRequest,TargetEnhanceRequest>{


    @Override
    public TargetEnhanceRequest execute(TargetEnhanceRequest request){

        JsonNode node= request.getResponseNode();

        ObjectMapper mapper= new ObjectMapper();

        String enhancedContent= mapper.convertValue(node,String.class);

        request.setEnhancedContent(enhancedContent);

        return request;

    }

}





@Component 
class TargetEnhanceResponseFactoryStep implements IPipelineStep<TargetEnhanceRequest,TargetEnhanceResponse>{


    @Override 
    public TargetEnhanceResponse execute(TargetEnhanceRequest request){

        String enhancedcontent= request.getEnhancedContent();

        return TargetEnhanceResponse.builder().enhancedContent(enhancedcontent).build();

    }

}


