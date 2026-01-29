package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.TranslateServiceWrapper;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TranslateClientStep implements IPipelineStep<FactCheckRequest,FactCheckRequest> {

    private TranslateServiceWrapper translateClient;

    @Override
    public FactCheckRequest execute(FactCheckRequest request){

        String requestContent= request.getProcessedContent();

        String translatedContent= translateClient.translate(requestContent,"eng");

        request.setProcessedContent(translatedContent);

        return request;


    }


    
}
