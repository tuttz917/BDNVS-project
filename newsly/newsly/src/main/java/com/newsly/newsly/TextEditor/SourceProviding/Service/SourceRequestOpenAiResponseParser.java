package com.newsly.newsly.TextEditor.SourceProviding.Service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;

import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SourceRequestOpenAiResponseParser implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{

    @Override 
    public SourceProvideRequest execute(SourceProvideRequest request){

        log.info("parser step");

        JsonNode node= request.getResponseNode();

        ObjectMapper mapper= new ObjectMapper();

        try{
        JsonNode contentNode= mapper.readTree(node.asText());
        log.info(contentNode.toPrettyString());

        List<String> links= mapper.convertValue(contentNode,new TypeReference<List<String>>() {
        });

        request.setSelectedLinks(links);

        return request;}catch(Exception e){
            log.info("eroare la deserializare");
            throw new RuntimeException();
        }

    }

}