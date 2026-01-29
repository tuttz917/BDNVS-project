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
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Component
public class EmbeddingService  {

    
    public float[] embeddSentence(String content){

        EmbeddingRequest embeddingRequest= EmbeddingRequest.builder().content(content).build();

        String json= JsonUtil.safeToJson(embeddingRequest);

        HttpRequest httpRequest= HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8000/api/py-service/text-embedding"))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .header("Content-type", "application/json")
                            .build();
        
        
        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);

        ObjectMapper mapper= new ObjectMapper();

        try{
        EmbeddingResponse embeddingResponse= mapper.readValue(response.body(), EmbeddingResponse.class);
            
        log.info(embeddingResponse.toString());
        
        return embeddingResponse.getEmbeddings();

        }catch(Exception e){
            log.warn("eroare la deserializare");
            throw new RuntimeException("eroare la maparea raspunsului de embedding");
        }

        

    }

}


@Data
@NoArgsConstructor
class EmbeddingResponse{

    private float[] embeddings;

}

@Builder
@Data
class EmbeddingRequest{
    private String content;
}