package com.newsly.newsly.TextEditor.SourceProviding.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceProvideRequest;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class SourceSerpRegistryStep implements IPipelineStep<SourceProvideRequest,SourceProvideRequest>{

    @Qualifier("serpRegistry")
    private FunctionRegistry<String,List<String>> serpRegistry;
    

    @Override 
    public SourceProvideRequest execute(SourceProvideRequest request){

        log.info("serp step");

        String search= request.getRawContent();

        List<String> links= serpRegistry.applyFirst(search);

        request.setLinks(links);

        return request;

    }

}
