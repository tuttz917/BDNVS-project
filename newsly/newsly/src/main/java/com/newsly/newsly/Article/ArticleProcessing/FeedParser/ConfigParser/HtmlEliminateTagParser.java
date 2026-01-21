package com.newsly.newsly.Article.ArticleProcessing.FeedParser.ConfigParser;

import java.util.function.Function;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.newsly.newsly.Article.DataObjects.TagData;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component

@Slf4j
@NoArgsConstructor
public class HtmlEliminateTagParser implements Function<TagData,String>{


    @Override
    public String apply(TagData tagData){

        log.info("HtmlTagEliminate parser");

        String rawContent=(String) tagData.getData();
        String tag=tagData.getTag();

        org.jsoup.nodes.Element doc= Jsoup.parse(rawContent);

        doc.select(tag).remove();

        String content= doc.text();

        return content;

    }


}