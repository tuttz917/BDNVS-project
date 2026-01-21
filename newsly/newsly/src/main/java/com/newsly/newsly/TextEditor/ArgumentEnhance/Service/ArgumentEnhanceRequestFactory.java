package com.newsly.newsly.TextEditor.ArgumentEnhance.Service;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentAdditionDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentContradictDto;
import com.newsly.newsly.TextEditor.ArgumentEnhance.Data.ArgumentEnhanceRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArgumentEnhanceRequestFactory {

    
    public ArgumentEnhanceRequest mapFromContradictionRequest(ArgumentContradictDto request){

        log.info("argument contradiction Factory");

        String context= request.getContext();
        String target= request.getTarget();
        String type= "contradict";

        return ArgumentEnhanceRequest.builder().context(context).target(target).type(type).build();

    }

    public ArgumentEnhanceRequest mapFromAdditionRequest(ArgumentAdditionDto request){

        log.info("argument Addition Factory");

        String context= request.getContext();
        String target= request.getTarget();
        String type= "addition";

        return ArgumentEnhanceRequest.builder().context(context).target(target).type(type).build();

    }
}
