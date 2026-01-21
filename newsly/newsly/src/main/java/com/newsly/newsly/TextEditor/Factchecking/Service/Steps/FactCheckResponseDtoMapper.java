package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import java.util.List;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckResponseDto;
import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

@Component
public class FactCheckResponseDtoMapper implements IPipelineStep<FactCheckResponse,FactCheckResponseDto> {


    @Override
    public FactCheckResponseDto execute(FactCheckResponse response){

        String value= response.getValue();
        List<String> links= response.getLinks();


        return FactCheckResponseDto.builder()
                                .value(value)
                                .links(links)
                                .build();

    }
    
}
