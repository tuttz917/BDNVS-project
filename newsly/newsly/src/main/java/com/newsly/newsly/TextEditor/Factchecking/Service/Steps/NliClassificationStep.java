package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.NliClassification;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class NliClassificationStep implements IPipelineStep<FactCheckRequest,FactCheckRequest> {

    private NliClassification nliClassification;


    @Override 
    public FactCheckRequest execute(FactCheckRequest request){

        String premise= request.getSimilarityCheckResult().getSearchContent();
        String hyphtothesys= request.getProcessedContent();

        String label= nliClassification.apply(premise, hyphtothesys);

        request.setNliClassificationLabel(label);

        return request;
    }
    
}
