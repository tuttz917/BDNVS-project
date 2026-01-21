package com.newsly.newsly.TextEditor.RedisRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.Article.Models.Article;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import redis.clients.jedis.UnifiedJedis;

@Slf4j
@Component
@AllArgsConstructor
public class RedisArticleRepo {
    

    UnifiedJedis jedis;


    public void saveManyToSync(List<Article> article){

        ObjectMapper mapper= new ObjectMapper();

        try{
        String json= mapper.writeValueAsString(article);
        
        jedis.lpush("sync_list:article",json);
        jedis.expire("sync_list:article",60*5);

        }catch(Exception e){
            log.info("eroare la serializare");
            throw new RuntimeException();
        }
    }

    public void saveWithKeyandTTL(String key, Article article, Long ttl){

        log.info("salvam in cache");

        Map<String,String> fields= new HashMap<>();

        fields.put("title",article.getTitle());
        fields.put("description",article.getDescription());
        fields.put("category",article.getCategory());
        fields.put("language",article.getLanguage());
        fields.put("articleUrl",article.getArticleURL());
        fields.put("imageUrl",article.getImageURL());
        fields.put("pub_date",article.getPubDate());

        jedis.hset(key,fields);
        jedis.expire(key,ttl);

    }

    public void pushToList(String key, Article article, Long ttl){

        ObjectMapper mapper= new ObjectMapper();

        try{
        String json= mapper.writeValueAsString(article);
        
        jedis.lpush(key,json);
        jedis.expire(key,ttl);

        }catch(Exception e){
            log.info("eroare la serializare");
            throw new RuntimeException();
        }



    }

    public Article getArticleById(String id){

        Map<String,String> fieldsMap= jedis.hgetAll(id);

        return Article.builder()
                        .id(fieldsMap.get("id"))
                        .articleURL(fieldsMap.get("articleUrl"))
                        .title(fieldsMap.get("title"))
                        .description(fieldsMap.get("description"))
                        .language(fieldsMap.get("language"))
                        .pubDate(fieldsMap.get("pub_date"))
                        .category(fieldsMap.get("category"))
                        .build();

    }

}
