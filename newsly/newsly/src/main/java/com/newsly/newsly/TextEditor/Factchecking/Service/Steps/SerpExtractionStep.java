package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;


import java.util.List;


import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.SerpApiProvider;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SerpExtractionStep implements IPipelineStep<FactCheckRequest,FactCheckRequest> {

    private SerpApiProvider serpEngine;

    @Override 
    public FactCheckRequest execute(FactCheckRequest request){

        String requestContent= request.getRawRequest();

        List<String> links= serpEngine.getArticleLinks(requestContent);

        request.setLinks(links);

        return request;


    }



    
}
