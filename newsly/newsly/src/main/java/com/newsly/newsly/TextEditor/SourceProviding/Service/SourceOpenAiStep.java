package com.newsly.newsly.TextEditor.SourceProviding.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.TextEditor.GeneralServices.OpenAiService;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceRequestDto;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SourceOpenAiStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{


    private OpenAiService openAiService;
    @Qualifier("source-providing prompt")
    private String prompt;

    public SourceOpenAiStep(
            OpenAiService service,
            @Qualifier("source-providing prompt") String prompt) {
        this.openAiService = service;
        this.prompt = prompt;
    }

    @Override 
    public SourceProvideRequest execute(SourceProvideRequest request){

        log.info("ai step");

        String content= request.getProcessedContent();
        List<SerpArticleDto> articles= request.getArticles();

        SourceRequestDto dto= SourceRequestDto.builder().content(content).articles(articles).build();

        log.info(dto.toString());

        JsonNode node=  openAiService.applyCompletion(prompt, dto);
        
        request.setResponseNode(node);

        return request; 

    }

}