package com.newsly.newsly.TextEditor.SourceProviding.Service;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.SourceProviding.Data.RawSourceProvideRequest;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor
public class SourceProvideRequestFactoryStep implements IPipelineStep<RawSourceProvideRequest,SourceProvideRequest>{

    @Override 
    public SourceProvideRequest execute(RawSourceProvideRequest rawRequest){


        log.info("factory step");

        String rawRequestContent= rawRequest.getContent();
        
        return SourceProvideRequest.builder().rawContent(rawRequestContent).processedContent(rawRequestContent).build();

    }

}