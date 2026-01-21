package com.newsly.newsly.Article.DataObjects;

import java.util.Objects;


import lombok.Builder;



@Builder
public record ArticleDto(String title, String category, String articleURL,
String sourceURL, String language, String description, String imageURL,String pubDate) {

    public ArticleDto(String title,String category,String articleURL,String sourceURL,String language,String description, String imageURL,String pubDate){
            this.title=Objects.requireNonNull(title);
            this.category=Objects.requireNonNull(category);
            this.articleURL=Objects.requireNonNull(articleURL);
            this.sourceURL=Objects.requireNonNull(sourceURL);
            this.language=Objects.requireNonNull(language);
            this.pubDate=pubDate;
            this.description=description;
            this.imageURL=imageURL;
    }


} 



    

