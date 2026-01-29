package com.newsly.newsly.Article.Models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection="")
public class DeletedArticle {

    @Id
    String id;

    @Field(name="article_url")
    String articleUrl;
    
}
