package com.newsly.newsly.TextEditor.SourceProviding.Service;


import java.util.List;

import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.LinkContentExtractor;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class SourceContentExtractorStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{

    private LinkContentExtractor contentExtractor;


    @Override 
    public SourceProvideRequest execute(SourceProvideRequest request){

        log.info("content extractor step");

        log.info(request.toString());

        List<String> links= request.getLinks();

        List<SerpArticleDto> articleDtos= contentExtractor.extractContentFrom(links);

        

        request.setArticles(articleDtos);

        return request;

    }

}