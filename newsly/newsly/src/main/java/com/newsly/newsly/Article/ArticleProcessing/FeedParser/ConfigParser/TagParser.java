package com.newsly.newsly.Article.ArticleProcessing.FeedParser.ConfigParser;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import com.newsly.newsly.Article.DataObjects.TagData;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public
class TagParser{

    private @Qualifier("tagParserRegistry") FunctionRegistry<TagData,String> tagParserRegistry;

    public String parseByTagConfig(Element element, String config) throws Exception{

        if(config==null || config.length()==0){
            throw new Exception("nu exista tag pentru aceast atribut");
        }

        log.info("suntem in helper");

        String characters="@!#";

        Queue<String> q_tags=new LinkedList<>();
        Queue<String> q_chars=new LinkedList<>();

        for(char c: config.toCharArray()){


            if (characters.indexOf(c)!=-1){

                String [] configArray= config.split(Character.toString(c));

                q_tags.add(configArray[0]);
                q_chars.add(Character.toString(c));

                config=configArray[1];
            }

        }
    
        q_tags.add(config);

            System.out.println(q_tags);
            System.out.println(q_chars);


    String elementTag=q_tags.peek();
    q_tags.remove();
    

    
    Element mainTagElement= (Element) element.getElementsByTagName(elementTag).item(0);
    Object currentData= mainTagElement;


    if(!q_tags.isEmpty()){
    
    while(!q_tags.isEmpty()){

        String currentTag= q_tags.peek();
        String configTag=q_chars.peek();

        q_chars.remove();
        q_tags.remove();
        
        log.info(configTag);

        TagData tagData=null;

        if(currentData instanceof String string){
             tagData=TagData.builder()
                        .character(configTag)
                        .data(string)
                        .tag(currentTag)
                        .build();
        }

        if(currentData instanceof Element element1){

            tagData=TagData.builder()
                        .character(configTag)
                        .data(element1)
                        .tag(currentTag)
                        .build();
        }

        

        currentData=tagParserRegistry.applyFirst(tagData);
        

    } }

    else{

        currentData= mainTagElement.getTextContent();
    
    }

        return (String) currentData;

    }
}
