package com.newsly.newsly.Article.ArticleProcessing;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.newsly.newsly.Article.DataObjects.FeedData;
import com.newsly.newsly.Article.DataObjects.FeedProcessRequest;
import com.newsly.newsly.Article.Models.Article;
import com.newsly.newsly.Article.Repo.ArticleRepo;
import com.newsly.newsly.TextEditor.RedisRepo.RedisArticleRepo;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;
import com.newsly.newsly.library.Pipelines.Pipeline;
import com.newsly.newsly.library.Validators.ValidationChain;


@Configuration
public class FeedToArticlePipelineConfig {




    @Bean(name="articleProcessingPipeline")
    public Pipeline<FeedData, List<Article>> feedToArticlePipeline(
                                                        FeedProcessRequestInitializer initializer
                                                        ,FeedContentExtractor contentExtractor 
                                                        , @Qualifier("feedToArticleParserRegistry") FunctionRegistry<FeedProcessRequest, List<Article>> feedToArticleParserRegistry
                                                        , ValidationChain<Article> validationChain
                                                        , ArticleRepo repo
                                                        , RedisArticleRepo cache
                                                        ){


            return Pipeline.<FeedData>builder()
                                    .then(initializer)
                                    .then(contentExtractor)
                                    .then(feedToArticleParserRegistry::applyFirst)
                                    .then(validationChain::executeMany)
                                    .thenConsume(repo::saveAll)
                                    .thenConsume(cache::saveManyToSync)
                                    .build();
                                                
    }
    
}
