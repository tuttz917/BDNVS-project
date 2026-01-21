package com.newsly.newsly.TextEditor.GeneralServices;

import org.springframework.stereotype.Component;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LabelResolver {

    private String applyForNeutral(String value){

    log.info("neutral label");
        return "neutral";

    }


    private String applyForContradiction(String value){

    
    log.info("contradiction label");

        
    if(value.equals("false")){

        log.info("nu putem infirma");

        return "neutral";

    }


    else if(value.equals("true")){

        log.info("putem infirma");
        

        return "false";


    }


    log.info("similarity result corupt");
    
    return "neutral";

    }


    private String applyForEntailment(String value){

    log.info("entailment label ");

    return value;

    

    }


    public String resolveFor(String value, String label){

        switch (label) {
            case "contradiction":
                return this.applyForContradiction(value);
                
            case "entailment":
                return this.applyForEntailment(value);
                
            case "neutral":
                return this.applyForNeutral(value);

            default:
                break;
        }

        log.info("label nerezolvabil");
        throw new RuntimeException();

    }



}