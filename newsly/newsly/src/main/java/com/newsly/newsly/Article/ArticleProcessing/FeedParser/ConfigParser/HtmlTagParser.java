package com.newsly.newsly.Article.ArticleProcessing.FeedParser.ConfigParser;

import java.util.function.Function;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.newsly.newsly.Article.DataObjects.TagData;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@NoArgsConstructor
public
class HtmlTagParser implements Function<TagData, String>{


    @Override 
    public String apply(TagData tagData){

        log.info("htmlTag parser");

        Element data=(Element) tagData.getData();

        String rawContent=data.getTextContent();

    if(rawContent.startsWith("<![CDATA[")){
        rawContent=rawContent.substring(9,rawContent.length()-3);
    }
        log.info(rawContent);

        String tag=tagData.getTag();

        log.info("Tagul: "+ tag);
        


        org.jsoup.nodes.Element doc=Jsoup.parse(rawContent);

        log.info(doc.html());

        String content = doc.selectFirst(tag).text();

        log.info(content);

        return content;
    }


}