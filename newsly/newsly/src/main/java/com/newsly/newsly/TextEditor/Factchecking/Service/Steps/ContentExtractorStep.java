package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import java.util.List;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.TextEditor.GeneralServices.LinkContentExtractor;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ContentExtractorStep implements IPipelineStep<FactCheckRequest,FactCheckRequest> {

    private LinkContentExtractor contentExtractor;

    @Override 
    public FactCheckRequest execute(FactCheckRequest request){

        List<String> articleLinks= request.getLinks();

        List<SerpArticleDto> articles= contentExtractor.extractContentFrom(articleLinks);

        request.setArticles(articles);  

        return request;

    }

    
}
