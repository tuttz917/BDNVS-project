package com.newsly.newsly.TextEditor.GeneralServices;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.Utils.HttpUtil;




import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
@Component 
public class SerpApiProvider  {

    

    public List<String> getArticleLinks(String search){

        log.info("serp extraction");


        String url = String.format(
    "https://serpapi.com/search?engine=google&q=%s&api_key=%s",
    URLEncoder.encode(search, StandardCharsets.UTF_8),
    "a377e85e6c33e2a86fca46b83949105b0b0fa334af1fdef4d954094bfd58687e"
);


    log.info("eroare1") ;       

        HttpRequest httpRequest= HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Content-type", "application/json")
                            .GET()
                            .build();
        
        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);
        
        log.info("eroare" );

        String jsonString= response.body();
                
        

        ObjectMapper mapper= new ObjectMapper(); 
        
        try{

        List<String> links= mapper.readTree(jsonString)
                                .get("organic_results")
                                .findValuesAsText("link");
        
        links.stream().forEach(link->log.info(link));
        
        return links;

        }catch(Exception e){
            throw new RuntimeException("eroare la mapparea json-ului din serpApi");
        }

        
    }

}



