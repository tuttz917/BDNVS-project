package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.GeneralServices.LabelResolver;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class FactCheckLabelResolverStep implements IPipelineStep<FactCheckRequest,FactCheckRequest> {
    
    LabelResolver labelResolver;

    @Override
    public FactCheckRequest execute(FactCheckRequest request){

        String value= request.getSimilarityCheckResult().getValue();
        String label= request.getNliClassificationLabel();

        String finalValue= this.labelResolver.resolveFor(value, label);

        if (finalValue=="neutral"){
            log.info("nu putem concluziona din datele cached");
            throw new RuntimeException();
        }

        request.setCheckValue(finalValue);
        request.setSelectedLinks(request.getSimilarityCheckResult().getLinks());

        return request;


    }


}
