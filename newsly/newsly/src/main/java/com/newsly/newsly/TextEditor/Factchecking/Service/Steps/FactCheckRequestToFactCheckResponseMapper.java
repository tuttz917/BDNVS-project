package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

@Component
public class FactCheckRequestToFactCheckResponseMapper implements IPipelineStep<FactCheckRequest,FactCheckResponse> {


    @Override 
    public FactCheckResponse execute(FactCheckRequest dto){

        UUID uuid= UUID.randomUUID();

        String id= uuid.toString();

        return FactCheckResponse.builder()
                                .id(id)
                                .embedding(dto.getEmbedding())
                                .content(dto.getProcessedContent())
                                .value(dto.getCheckValue())
                                .links(dto.getSelectedLinks())
                                .build();

    }

    
}
