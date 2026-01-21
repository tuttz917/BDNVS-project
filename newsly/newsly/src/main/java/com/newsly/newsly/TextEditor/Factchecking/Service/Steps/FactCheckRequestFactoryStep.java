package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;


import org.springframework.stereotype.Component;


import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.RawFactCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FactCheckRequestFactoryStep implements IPipelineStep<RawFactCheckRequest,FactCheckRequest> {

    


    @Override 
    public FactCheckRequest execute(RawFactCheckRequest rawRequest){

        String content= rawRequest.getContent();
        
        FactCheckRequest request= FactCheckRequest.builder()
                                                    .rawRequest(content)
                                                    .processedContent(content)
                                                    .language("ro")
                                                    .build();

        

        return request;

    }


    
}
