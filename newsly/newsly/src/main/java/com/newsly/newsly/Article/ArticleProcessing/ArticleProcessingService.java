package com.newsly.newsly.Article.ArticleProcessing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.Article.DataObjects.FeedData;
import com.newsly.newsly.Article.Models.Article;
import com.newsly.newsly.Article.Repo.ArticleRepo;
import com.newsly.newsly.library.Pipelines.Pipeline;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleProcessingService {

    Pipeline<FeedData,List<Article>> articleProcessingPipeline;
    ArticleRepo repo;
    public ArticleProcessingService(@Qualifier("articleProcessingPipeline")Pipeline<FeedData,List<Article>> articleProcessingPipeline,ArticleRepo repo){

        this.articleProcessingPipeline=articleProcessingPipeline;
        this.repo=repo;

    }

    @Scheduled(fixedDelay = 20000)
    public void extractFeedData() {

        log.info("serviciu de extragere al articolelor");

        HttpClient httpClient= HttpClient.newHttpClient();

        HttpRequest request= HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:3000/api/feeds"))
                            .build();
        try{

        
        HttpResponse<String> response= httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String body= response.body();

        ObjectMapper mapper= new ObjectMapper();

        try{

            List<FeedData> feedData= mapper.readValue(body,new TypeReference<List<FeedData>>() {
            });

            List<List<Article>> articles= articleProcessingPipeline.runMany(feedData).getResultData();

            log.info(articles.toString());

            List<Article> mappedArticles= articles.stream().flatMap(list->list.stream()).collect(Collectors.toList());
            repo.saveAll(mappedArticles);



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
