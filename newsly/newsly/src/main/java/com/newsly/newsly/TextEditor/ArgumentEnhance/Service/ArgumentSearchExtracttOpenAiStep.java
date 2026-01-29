package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsly.Utils.Pair;
import com.newsly.newsly.GeneralServices.OpenAiService;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceSearchExtractDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentSearchExtracttOpenAiStep implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceRequest>{

    OpenAiService service;
    String prompt;

    public ArgumentSearchExtracttOpenAiStep(OpenAiService service, @Qualifier("search-extracting prompt") String prompt){

        this.service=service;
        this.prompt=prompt;

    }

    @Override
    public ArgumentEnhanceRequest execute(ArgumentEnhanceRequest request){

        log.info("search extracting step");

        String context= request.getContext();
        Pair<Integer,Integer> targetBounds= request.getTargetBounds();
        List<SerpArticleDto>articles= request.getArticles();
        
        ArgumentEnhanceSearchExtractDto dto= ArgumentEnhanceSearchExtractDto.builder().context(context).targetBounds(targetBounds).articles(articles).build();

        JsonNode searchExtractNode= service.applyCompletion(prompt, dto);

        request.setSearchResponseNode(searchExtractNode);

        return request;

    }

}