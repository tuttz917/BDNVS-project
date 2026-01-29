package com.newsly.newsly.GeneralServices;

import java.net.URI;
import java.net.http.HttpRequest;

import java.net.http.HttpResponse;
import java.util.List;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.Utils.HttpUtil;
import com.newsly.Utils.JsonUtil;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@NoArgsConstructor
@Component
public class LinkContentExtractor  {



    public List<SerpArticleDto> extractContentFrom(List<String> links){

        log.info("contentExtractor");

        String json= JsonUtil.safeToJson(links);

        HttpRequest httpRequest= HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8000/api/py-service/extract-data-from-url"))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();


        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);

        try{

            ObjectMapper mapper= new ObjectMapper();
            ContentExtractDto dto=mapper.readValue(response.body(),ContentExtractDto.class);

            
            return dto.getArticles();

        }catch(Exception e){
            throw new RuntimeException();
        } 

    }

    
}


@ToString
@Data
@NoArgsConstructor
class ContentExtractDto{

    private List<SerpArticleDto> articles;

}