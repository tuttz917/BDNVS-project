package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckResponseDto;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

@Component
public class FactCheckResponseMapperFromSimilarity implements IPipelineStep<FactCheckRequest,FactCheckResponseDto> {



    @Override
    public FactCheckResponseDto execute(FactCheckRequest request){
        return FactCheckResponseDto.builder().value(request.getCheckValue()).build();
    }
    
}
