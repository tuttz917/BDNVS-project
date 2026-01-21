package com.newsly.newsly.Article.ArticleProcessing.FeedParser;


import java.io.StringReader;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newsly.newsly.Article.ArticleProcessing.FeedParser.ConfigParser.TagParser;

import com.newsly.newsly.Article.DataObjects.ArticleDto;

import com.newsly.newsly.Article.DataObjects.FeedProcessRequest;
import com.newsly.newsly.Article.DataObjects.RssFeedTags;
import com.newsly.newsly.Article.DataObjects.TagData;
import com.newsly.newsly.Article.Models.Article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;





@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@Slf4j
public class RssFeedParser {


    private final TagParser tagParser;
    private String canHandle="rss";
    
    public boolean canHandle(String source){
        return this.canHandle.equals(source);
    }

    
    public List<Article> apply(FeedProcessRequest feed) {

        log.info("checkpoint1");

        RssFeedTags tags= feed.getTags();

        List<Article> parsedArticles=new ArrayList<>();
        try{

            log.info("checkpoint2");

            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document document= builder.parse(new InputSource(new StringReader(feed.getFeedContent())));

            log.info("checkpoint3");

            NodeList nodelist=document.getElementsByTagName(tags.getItemTag());

            

            for(int i=0;i<nodelist.getLength();i++){
            
            log.info("checkpoint4");

            Element element=(Element) nodelist.item(i);

            String title=null;
            
            try{
            title= tagParser.parseByTagConfig(element,tags.getTitleTag());
            log.info(title);
            }catch(Exception e){
                log.info(e.getMessage());
            }

            String articleUrl=null;
            
            try{
            articleUrl= tagParser.parseByTagConfig(element, tags.getLinkTag());

            log.info(articleUrl);
            }catch(Exception e){
                log.info(e.getMessage());
            }

            String description=null;

            try{
            description= tagParser.parseByTagConfig(element, tags.getDescriptionTag());

            log.info(description);}catch(Exception e){
                log.info(e.getMessage());
            }
            
            String imageUrl=null;

            try{
            imageUrl= tagParser.parseByTagConfig(element, tags.getImageTag());
            log.info(imageUrl);
            } catch(Exception e){
                log.info(e.getMessage());
            }

            String pubDate=null;

            try{
                pubDate=tagParser.parseByTagConfig(element, tags.getPubDateTag());
                log.info(pubDate);
            }catch(Exception e){
                log.info(e.getMessage());
            }

            UUID uuid= UUID.randomUUID();
            String id= uuid.toString();

            

            Article article= Article.builder()
                            .id(id)
                            .title(title)
                            .articleURL(articleUrl)
                            .sourceURL(feed.getSource())
                            .category("politica-interna")
                            .language("ro")
                            .description(description)
                            .imageURL(imageUrl)
                            .pubDate(pubDate)
                            .build();

            parsedArticles.add(article);

            }

        


        }catch(Exception e){

            log.info(e.getMessage());

        }
        
        return parsedArticles;
    

    }

}



