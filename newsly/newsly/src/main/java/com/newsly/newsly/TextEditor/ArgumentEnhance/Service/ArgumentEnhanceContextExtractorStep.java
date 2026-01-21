package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
import com.newsly.newsly.TextEditor.GeneralServices.LinkContentExtractor;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentEnhanceContextExtractorStep implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceRequest>{

    private FunctionRegistry<String,List<String>> serpRegistry;
    private LinkContentExtractor contentExtractor;

    public ArgumentEnhanceContextExtractorStep(@Qualifier("serpRegistry") FunctionRegistry<String,List<String>> serpRegistry, LinkContentExtractor contentExtractor){

        this.serpRegistry=serpRegistry;
        this.contentExtractor=contentExtractor;

    }

    @Override 
    public ArgumentEnhanceRequest execute(ArgumentEnhanceRequest request){
        
        log.info("content extractor step");

        String search= request.getSearch(); 

        List<String> links=serpRegistry.applyFirst(search);

        List<SerpArticleDto> dtos= contentExtractor.extractContentFrom(links);

        request.setArticles(dtos);

        return request;



    }


}
