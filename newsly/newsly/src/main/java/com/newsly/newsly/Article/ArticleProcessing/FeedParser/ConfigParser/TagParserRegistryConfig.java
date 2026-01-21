package com.newsly.newsly.Article.ArticleProcessing.FeedParser.ConfigParser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.Article.DataObjects.TagData;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;

@Configuration
public class TagParserRegistryConfig {


    
    @Bean(name="tagParserRegistry")
    FunctionRegistry<TagData, String> tagParserRegistry(AttributeParser attributeParser, HtmlEliminateTagParser eliminateHtmlParser, HtmlTagParser htmlTagParser){


        return FunctionRegistry.<TagData,String>builder()
                                            .addFunction(attributeParser::apply,"@")
                                            .addFunction(htmlTagParser::apply,"#")
                                            .addFunction(eliminateHtmlParser::apply,"!")
                                            .build()
                                            .applyBy((tagData)->tagData.getCharacter());


    }

}
