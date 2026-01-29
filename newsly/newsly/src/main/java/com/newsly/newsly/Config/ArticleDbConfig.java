package com.newsly.newsly.Config;

import java.util.concurrent.TimeUnit;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;


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

    @Bean(name="mongoClientDatabaseFactory")
    SimpleMongoClientDatabaseFactory simpleMongoClientDatabaseFactory(@Qualifier("articleMongoClient") MongoClient client){

        return new SimpleMongoClientDatabaseFactory(client,"articles-storage");

    }

    @Bean(name="articleDbTransactionManager")
    PlatformTransactionManager mongoTransactionManager(@Qualifier("mongoClientDatabaseFactory") SimpleMongoClientDatabaseFactory factory ){


        return new MongoTransactionManager(factory);

    }


    @SuppressWarnings("null")
    @Bean(name="articleMongoTemplate")
    MongoTemplate mongoTemplate(@Qualifier("articleMongoClient") MongoClient mongoClient){

        return new MongoTemplate(mongoClient,"articles-storage");

    }


}
