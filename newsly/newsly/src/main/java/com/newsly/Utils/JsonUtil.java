package com.newsly.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil{

    public static String safeToJson(Object object){
        
        ObjectMapper mapper= new ObjectMapper();
        
        try{
            String json= mapper.writeValueAsString(object);
            return json;
        }catch(JsonProcessingException exception){
            log.info("eroare la serializare in json");
            throw new RuntimeException("eroare la serializare in json ");
        }

    }

}
