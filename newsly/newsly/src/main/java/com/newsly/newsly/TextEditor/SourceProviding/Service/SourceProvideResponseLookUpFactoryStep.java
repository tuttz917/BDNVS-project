package com.newsly.newsly.TextEditor.SourceProviding.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;

import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SourceProvideResponseLookUpFactoryStep implements IPipelineStep<SourceProvideRequest,SourceProvideResponse>{

 

    @Override 
    public SourceProvideResponse execute(SourceProvideRequest request){
        
        log.info("factory steps");

        List<String> links= request.getSelectedLinks();

        UUID uuid= UUID.randomUUID();
        String id= uuid.toString();

        log.info("mere");

        SourceProvideResponse response=  SourceProvideResponse.builder().id(id).links(links).embedding(request.getEmbedding()).content(request.getProcessedContent()).build();

        log.info(response.toString());

        

        return response;

    }

}
