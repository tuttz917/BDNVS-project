package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class SerpRegistryStep implements IPipelineStep<FactCheckRequest,FactCheckRequest> {


    @Qualifier("serpRegistry")
    private FunctionRegistry<String,List<String>> serpRegistry;

    @Qualifier 
    public FactCheckRequest execute(FactCheckRequest request){

        String search=request.getRawRequest();

        List<String> links= serpRegistry.applyFirst(search);

        request.setLinks(links);
        
        return request;

    }
    
    
}
