package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Factchecking.Data.RawFactCheckRequest;
import com.newsly.newsly.TextEditor.GeneralServices.WordCountPredicate;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor  
public class WordCountStep implements Predicate<RawFactCheckRequest> {

    private WordCountPredicate predicate;


    @Override 
    public boolean test(RawFactCheckRequest rawRequest){

        String requestContent= rawRequest.getContent();

        boolean validated= predicate.test(requestContent,20, 0);

        return validated;    

    }

    
}
