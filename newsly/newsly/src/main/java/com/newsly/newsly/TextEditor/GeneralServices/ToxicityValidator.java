package com.newsly.newsly.TextEditor.GeneralServices;

import java.net.URI;


import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.Utils.HttpUtil;
import com.newsly.Utils.JsonUtil;
import com.newsly.newsly.TextEditor.Factchecking.Data.ToxicValidationDto;
import com.newsly.newsly.TextEditor.Factchecking.Data.ToxicityRaport;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component 
@NoArgsConstructor
public class ToxicityValidator {

    @Autowired
    private ToxicityRaportPredicate toxicRaportPredicate;

    

    
    public boolean test(String content){

        log.info("toxicity predicate");


        ToxicValidationDto dto=  ToxicValidationDto.builder()
                                                    .content(content)
                                                    .build();

        String json= JsonUtil.safeToJson(dto);

        log.info("check 1");

        HttpRequest httpRequest= HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:8000/api/py-service/toxic-validation"))
                                    .POST(HttpRequest.BodyPublishers.ofString(json))
                                    .header("Content-type", "application/json")
                                    .build();
        
        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);

        log.info("check2");

        ObjectMapper mapper= new ObjectMapper();

        try{

            ToxicityRaport raport= mapper.readValue(response.body(),ToxicityRaport.class);

            log.info(raport.toString());

            if(toxicRaportPredicate.test(raport)){

                return true;

            }

            return false;

        }catch(Exception e){
            log.info("eroare la deserializare");
            throw new RuntimeException("exceptie la serviciul de testare-toxicitate");
        }
        

    }

}




@Slf4j
@Component
@NoArgsConstructor
class ToxicityRaportPredicate implements Predicate<ToxicityRaport>{

        private double MAX_TOXICITY= 0.6;
        private double MAX_SEVERE_TOXICITY= 0.6;
        private double MAX_OBSCENE= 0.6;
        private double MAX_THREAT= 0.6;
        private double MAX_INSULT= 0.6;
        private double MAX_IDENTITY_ATACK= 0.6;

    @Override
    public boolean test(ToxicityRaport raport){

        log.info("toxicity raport predicate");

        if(raport.getToxicity()>MAX_TOXICITY || raport.getSevereToxicity()>MAX_SEVERE_TOXICITY || raport.getObscene()>MAX_OBSCENE
        || raport.getThreat()>MAX_THREAT || raport.getInsult()>MAX_INSULT || raport.getIdentityAttack()>MAX_IDENTITY_ATACK){

            return false;

        }

        return true;

    }

}
