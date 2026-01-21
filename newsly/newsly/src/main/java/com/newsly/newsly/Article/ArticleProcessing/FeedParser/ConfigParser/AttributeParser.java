package com.newsly.newsly.Article.ArticleProcessing.FeedParser.ConfigParser;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.newsly.newsly.Article.DataObjects.TagData;




@Component
public 
class AttributeParser implements Function<TagData,String> {

    public AttributeParser(){}

    Logger log= LoggerFactory.getLogger(AttributeParser.class);
    
    @Override 
    public String apply(TagData tagData){

        log.info("attribute parser");

        Element data= (Element) tagData.getData();

        String content=data.getAttribute(tagData.getTag());

        return content;

    }

}
