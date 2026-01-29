package com.newsly.newsly.GeneralServices;




import java.net.URI;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.Utils.HttpUtil;
import com.newsly.Utils.JsonUtil;


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Component
public class NliClassification  {
    

    public String apply(String premise, String hypotesis){

        log.info("nliClassification");

        NliClassificationDto dto= NliClassificationDto.builder().premise(premise).hypothesis(hypotesis).build();
        
        String json= JsonUtil.safeToJson(dto);

        HttpRequest httpRequest= HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8000/api/py-service/nli-check"))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();
        
        
        ObjectMapper mapper= new ObjectMapper();

        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);

        try{
        
        String node= response.body();

        log.info(node);
        
        String label= mapper.readValue(response.body(), String.class);

        return label;

        }catch(Exception e){
            log.info("Eroare la deserializare");
            throw new RuntimeException();
        }

        
    }

}

@Data
@Builder
class NliClassificationDto{

    private String premise;
    private String hypothesis;

}





