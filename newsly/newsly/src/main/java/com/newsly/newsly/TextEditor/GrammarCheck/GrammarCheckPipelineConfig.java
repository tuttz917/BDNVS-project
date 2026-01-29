package com.newsly.newsly.TextEditor.GrammarCheck;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.GeneralServices.OpenAiService;
import com.newsly.newsly.TextEditor.GrammarCheck.Data.GrammarCheckRequest;
import com.newsly.newsly.TextEditor.GrammarCheck.Data.RawGrammarCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;
import com.newsly.newsly.library.Pipelines.Pipeline;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Configuration
public class GrammarCheckPipelineConfig{

    private GrammarCheckRequestFactoryStep factory;
    private GrammarCheckRequestAiStep openaiStep;
    private GrammarCheckResponseParserStep parserStep;

    @Bean("grammarCheckPipeline")
    public Pipeline<RawGrammarCheckRequest,GrammarCheckRequest> build(){

        return Pipeline.<RawGrammarCheckRequest>builder()
                                            .then(factory)
                                            .then(openaiStep)
                                            .then(parserStep)
                                            .build();

    }

}





@Component
class GrammarCheckRequestFactoryStep implements IPipelineStep<RawGrammarCheckRequest,GrammarCheckRequest>{


    @Override
    public GrammarCheckRequest execute(RawGrammarCheckRequest rawRequest){

        log.info("factory step");

        String content= rawRequest.getContent();

        return GrammarCheckRequest.builder()
                                .rawContent(content)
                                .processedContent(content)
                                .build();

    }

}


@Component
class GrammarCheckRequestAiStep implements IPipelineStep<GrammarCheckRequest,GrammarCheckRequest>{

    OpenAiService service;
    String prompt;
        
    public GrammarCheckRequestAiStep(OpenAiService service, @Qualifier("grammar-check prompt")String prompt){

        this.service= service;
        this.prompt=prompt;

    }
        

        @Override 
        public GrammarCheckRequest execute(GrammarCheckRequest request){

            log.info("openai step");

            String processedContent= request.getProcessedContent();

            JsonNode node= service.applyCompletion(prompt, processedContent);

            request.setResponseNode(node);

            return request;

        }

}


@Component
class GrammarCheckResponseParserStep implements IPipelineStep<GrammarCheckRequest,GrammarCheckRequest>{

    @Override
    public GrammarCheckRequest execute(GrammarCheckRequest request){

        log.info("parser step");

        JsonNode node= request.getResponseNode();

        ObjectMapper mapper= new ObjectMapper();

        String responseContent= mapper.convertValue(node,String.class).trim();
        String processedContent= responseContent.substring(1,responseContent.length()-1);

        log.info(processedContent);

        request.setProcessedContent(processedContent);

        return request;

    }

}