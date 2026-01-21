package com.newsly.newsly.Config;

import java.util.concurrent.TimeUnit;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@EnableMongoRepositories(
    basePackages = "com.newsly.newsly.Article.Repo",
    mongoTemplateRef="articleMongoTemplate"
)
@Configuration
public class ArticleDbConfig  {

    
    @Bean(name="articleMongoClient")
    MongoClient mongoClient(){
    ConnectionString connectionString= new ConnectionString("mongodb://localhost:27017/");

    MongoClientSettings settings= MongoClientSettings.builder()
                                            .applyConnectionString(connectionString)
                                            .retryReads(true)
                                            .retryWrites(true)
                                            .applyToConnectionPoolSettings(builder->builder.maxSize(20)
                                                                                            .minSize(5)
                                                                                            .maxConnectionIdleTime(4, TimeUnit.MINUTES))
                                                                                            .build();               

        
        return MongoClients.create(settings);
        
    }

    @SuppressWarnings("null")
    @Bean(name="articleMongoTemplate")
    MongoTemplate mongoTemplate(@Qualifier("articleMongoClient") MongoClient mongoClient){

        return new MongoTemplate(mongoClient,"articles-storage");

    }


}
