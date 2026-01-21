package com.newsly.newsly.TextEditor.SourceProviding.Service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;

import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SourceProvideResponseRecallFactoryStep implements IPipelineStep<SourceProvideRequest,SourceProvideResponse>{

    @Override 
    public SourceProvideResponse execute(SourceProvideRequest request){
        
        log.info("factory step");

        List<String> links= request.getSearchResponse().getLinks();

        SourceProvideResponse response=  SourceProvideResponse.builder().links(links).embedding(request.getEmbedding()).content(request.getProcessedContent()).build();

        log.info(response.toString());

        return response;

    }

}



