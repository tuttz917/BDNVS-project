package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.stereotype.Component;

import com.newsly.Utils.Pair;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentEnhanceExtractTargetBoundsStep implements IPipelineStep<ArgumentEnhanceRequest,ArgumentEnhanceRequest>{


    @Override
    public ArgumentEnhanceRequest execute(ArgumentEnhanceRequest request){

        log.info("target limits extractor");

        String context= request.getContext();
        String target= request.getTarget();

        Integer upperBound= context.indexOf(target);

        Integer lowerBound= upperBound + target.length();

        log.info(Integer.toString(upperBound));
        log.info(Integer.toString(lowerBound));

        Pair<Integer,Integer> limits=  Pair.<Integer,Integer>builder().first(upperBound).second(lowerBound).build();

        request.setTargetBounds(limits);

        return request;


    }

}
