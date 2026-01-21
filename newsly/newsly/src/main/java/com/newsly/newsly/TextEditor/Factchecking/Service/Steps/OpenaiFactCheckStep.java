package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequestDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.TextEditor.GeneralServices.OpenAiService;
import com.newsly.newsly.library.Pipelines.IPipelineStep;




@Component
public class OpenaiFactCheckStep implements IPipelineStep<FactCheckRequest,FactCheckRequest>{

    
    private OpenAiService service;
    @Qualifier("fact-check prompt")
    private String prompt;

    public OpenaiFactCheckStep(
            OpenAiService service,
            @Qualifier("fact-check prompt") String prompt
    ) {
        this.service = service;
        this.prompt = prompt;
    }

    @Override 
    public FactCheckRequest execute(FactCheckRequest request){

        String userRequest= request.getRawRequest();
        List<SerpArticleDto> articles= request.getArticles();

        FactCheckRequestDto dto= FactCheckRequestDto.builder().articles(articles).content(userRequest).build();

        JsonNode node= service.applyCompletion(this.prompt,dto);

        request.setResponseNode(node);

        return request;

    }
    
}
