package com.newsly.newsly.Article.ArticleProcessing;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import org.springframework.stereotype.Component;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.newsly.newsly.Article.DataObjects.FeedProcessRequest;

import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class FeedContentExtractor implements IPipelineStep<FeedProcessRequest,FeedProcessRequest>{


    @Override
    public FeedProcessRequest execute(FeedProcessRequest request){

        log.info("content extractor");        

        String url= request.getLink();
        

        HttpClient client= HttpClient.newHttpClient();

        HttpRequest httpRequest =    HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create(url))
                                .build();
        
        try{
        HttpResponse<String> response= client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        try{
        ObjectMapper mapper= new ObjectMapper();

        String encodedContent= response.body();
        
    

        request.setFeedContent(encodedContent);

            return request;
        }catch(Exception e){
            log.info("eroare la deserializare");
            throw new RuntimeException();
        }

        }catch(Exception e){

            log.info("eroare la primirea datelor");
            throw new RuntimeException();

        }


    
    }

}




