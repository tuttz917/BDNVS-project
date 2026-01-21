package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.GeneralServices.TextNormalizer;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
public class TextNormalizerStep implements IPipelineStep<FactCheckRequest, FactCheckRequest> {

    private TextNormalizer normalizer;

    @Override
    public FactCheckRequest execute(FactCheckRequest request){

        String requestContent= request.getProcessedContent();

        String normalizedContent= normalizer.normalizeText(requestContent);

        request.setProcessedContent(normalizedContent);

        return request;


    }
    
}
