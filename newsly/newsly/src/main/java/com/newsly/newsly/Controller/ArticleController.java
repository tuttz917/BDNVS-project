package com.newsly.newsly.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.newsly.newsly.Article.Models.Article;
import com.newsly.newsly.Article.Repo.ArticleRepo;
import com.newsly.newsly.TextEditor.RedisRepo.RedisArticleRepo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@RestController
public class ArticleController {
    
    RedisArticleRepo cache;
    ArticleRepo repo;
    UnifiedJedis jedis;

    @PostMapping("/api/v1/article")
    public Article getArticle(@RequestParam String id, @RequestBody LocationData locationData) {

        log.info("am primit cererea");

        Article article;

        Double latitude= locationData.getLatitude();
        Double longitude= locationData.getLongitude();

        try{
            article= repo.getArticleById(id);

        }catch(Exception e){
            log.info("baza de date nu raspunde");
            article= cache.getArticleById(id);
        }

        String feedkey= "article:"+article.getId();

            jedis.del(feedkey);

            jedis.geoadd("article:location", latitude,longitude,feedkey);

            cache.saveWithKeyandTTL(feedkey, article, new Long(60*30));

            return article;

    }

    @PostMapping("/api/v1/geoFeed")
    public List<Article> geoFeed(@RequestBody Double latitude, Double longitude) {

        GeoSearchParam params= new GeoSearchParam().fromLonLat(longitude,latitude).byRadius(50,GeoUnit.KM);

        List<GeoRadiusResponse> responses= jedis.geosearch("article:location",params);

        List<Article> articles= responses.stream().map(response->{

            Map<String,String> fieldsMap= jedis.hgetAll(response.getMemberByString());

            return Article.builder()
                        .id(fieldsMap.get("id"))
                        .articleURL(fieldsMap.get("articleUrl"))
                        .title(fieldsMap.get("title"))
                        .description(fieldsMap.get("description"))
                        .language(fieldsMap.get("language"))
                        .pubDate(fieldsMap.get("pub_date"))
                        .category(fieldsMap.get("category"))
                        .build();

        }).collect(Collectors.toList());

        return articles;
        
    }

    
    



}
