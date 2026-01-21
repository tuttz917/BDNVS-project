package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentEnhanceSearchExtractParser implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceRequest>{

    @Override
    public ArgumentEnhanceRequest execute(ArgumentEnhanceRequest request){

        log.info("search response parser");

        ObjectMapper mapper= new ObjectMapper();

        JsonNode node= request.getSearchResponseNode();

        String search= mapper.convertValue(node, String.class);

        request.setSearch(search);

        return request;


    }

}
