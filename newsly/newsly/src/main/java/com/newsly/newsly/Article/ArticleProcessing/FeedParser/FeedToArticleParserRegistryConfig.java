package com.newsly.newsly.Article.ArticleProcessing.FeedParser;

import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.newsly.newsly.Article.DataObjects.FeedProcessRequest;
import com.newsly.newsly.Article.Models.Article;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;



@Configuration
public class FeedToArticleParserRegistryConfig{


   
    @Bean(name="feedToArticleParserRegistry")
    FunctionRegistry<FeedProcessRequest, List<Article>> feedToArticleParserRegistry(RssFeedParser rssFeedParser){

            return FunctionRegistry.<FeedProcessRequest,List<Article>>builder()
                                            .addFunction(rssFeedParser::apply, "rss")
                                            .build()
                                            .applyBy((feed)->feed.getSource());

    }

}
