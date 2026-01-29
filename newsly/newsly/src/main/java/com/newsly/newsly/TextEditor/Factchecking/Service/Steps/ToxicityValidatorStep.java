package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.GeneralServices.ToxicityValidator;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ToxicityValidatorStep implements Predicate<FactCheckRequest> {

    private ToxicityValidator validator;

    @Override   
    public boolean test(FactCheckRequest request){

        String requestContent= request.getProcessedContent();

        boolean value= validator.test(requestContent);

        return value;
    }


    
}
