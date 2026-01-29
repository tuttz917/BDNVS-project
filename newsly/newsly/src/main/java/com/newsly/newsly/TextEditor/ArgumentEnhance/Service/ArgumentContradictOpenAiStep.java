package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsly.Utils.Pair;
import com.newsly.newsly.GeneralServices.OpenAiService;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequestDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentContradictOpenAiStep implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceRequest>{

    OpenAiService service;
    String prompt;

    public ArgumentContradictOpenAiStep(OpenAiService service, @Qualifier("argument-contradicting prompt") String prompt){

        this.service=service;
        this.prompt=prompt;

    }

    @Override
    public ArgumentEnhanceRequest execute(ArgumentEnhanceRequest request){

        log.info("argument contradicting step");

        String context= request.getContext();
        Pair<Integer,Integer> targetBounds= request.getTargetBounds();
        List<SerpArticleDto> articles= request.getArticles();

        ArgumentEnhanceRequestDto dto= ArgumentEnhanceRequestDto.builder().context(context).targetLimits(targetBounds).articleDtos(articles).build();

        JsonNode responseNode= service.applyCompletion(prompt, dto);

        request.setResponseNode(responseNode);

        return request;

    }

}