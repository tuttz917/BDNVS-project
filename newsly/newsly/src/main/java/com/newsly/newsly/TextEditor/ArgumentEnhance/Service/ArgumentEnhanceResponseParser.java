package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgummentEnhanceOpenAiResponseDto;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

@Component
@Slf4j
public class ArgumentEnhanceResponseParser implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceRequest>{

    @Override
    public ArgumentEnhanceRequest execute(ArgumentEnhanceRequest request){

        log.info("response parser");

        ObjectMapper mapper= new ObjectMapper();

        JsonNode node= request.getResponseNode();

        try{

        ArgummentEnhanceOpenAiResponseDto dto= mapper.readValue(node.asText(),ArgummentEnhanceOpenAiResponseDto.class);

        log.info(dto.getTarget());

        request.setProcessedArgument(dto.getTarget());
        request.setResponseSources(dto.getSources());

        return request;
        }catch(Exception e){
            log.info("eroare la deserializare");
            throw new RuntimeException();
        }


    }

}