package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.EmbeddingService;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmbeddingStep implements IPipelineStep<FactCheckRequest,FactCheckRequest>{

    private EmbeddingService service;

    @Override 
    public FactCheckRequest execute(FactCheckRequest request){

        String requestContent=request.getProcessedContent();

        float[] embeddings= service.embeddSentence(requestContent);

        request.setEmbedding(embeddings);

        return request;

    }

    
}
