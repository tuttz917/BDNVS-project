package com.newsly.newsly.Article.Models;




import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@Document(collection="articles")
public class Article   {
    
    @Id
    private String id;

    @Field(name="title")
    private String title;

    @Field(name="description")
    private String description;
    
    @Field(name="category")
    private String category;

    @Field(name="language")
    private String language;

    @Field(name="imageurl")
    private String imageURL;
    
    @Field(name="sourceurl")
    private String sourceURL;

    @Field(name="articleurl")
    private String articleURL;

    @Field(name="pub_date")
    private String pubDate;

}
