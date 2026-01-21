package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceResponseDto;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentEnhanceResponseMapper implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceResponseDto>{

    

    @Override 
    public ArgumentEnhanceResponseDto execute(ArgumentEnhanceRequest request){

        log.info("argument enhance response mapper");

        String argument= request.getProcessedArgument();
        List<String> sources= request.getResponseSources();

        return ArgumentEnhanceResponseDto.builder().target(argument).sources(sources).build();

    }

}
