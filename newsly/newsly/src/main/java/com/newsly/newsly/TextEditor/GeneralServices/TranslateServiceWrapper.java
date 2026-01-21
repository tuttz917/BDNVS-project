package com.newsly.newsly.TextEditor.GeneralServices;


import java.net.URI;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



import org.springframework.stereotype.Component;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.Utils.HttpUtil;
import com.newsly.Utils.JsonUtil;
import com.newsly.newsly.TextEditor.Factchecking.Data.TranslateServiceDto;

import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Component
public class TranslateServiceWrapper  {
    
    private String url="http://localhost:8000/api/py-service/translate";


    public String translate(String content, String targetLanguage) {

        log.info("translate client");

        
        
        TranslateServiceDto dto= TranslateServiceDto.builder()
                                                    .content(content)
                                                    .targetLanguage(targetLanguage)
                                                    .build();
        
        String json= JsonUtil.safeToJson(dto);

        

        
        HttpRequest httpRequest= HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type","application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
        
        
        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);

        

        ObjectMapper mapper= new ObjectMapper();

        try{
        
        log.info(response.body());
        
        String translatedContent= mapper.readValue(response.body(),String.class);
        
    
            
        log.info(translatedContent);

        return translatedContent;

        }catch(Exception e){
            log.warn("eroare in clientul de traducere");
            throw new RuntimeException("eroare la maparea raspunsului din translate");
            
        }
                        


    } 


}








