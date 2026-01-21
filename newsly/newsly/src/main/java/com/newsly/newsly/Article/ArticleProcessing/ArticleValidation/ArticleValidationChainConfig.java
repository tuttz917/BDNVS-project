package com.newsly.newsly.Article.ArticleProcessing.ArticleValidation;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.newsly.newsly.Article.Models.Article;
import com.newsly.newsly.library.Validators.ValidationChain;

@Configuration 
public class ArticleValidationChainConfig {


   
    @Bean(name="articleValidationChain")
    ValidationChain<Article> articleValidationChain(DuplicateUrl duplicateUrl, DuplicateTitle duplicateTitle) {

            return ValidationChain.<Article>builder()
                                    .then(duplicateUrl)
                                    .then(duplicateTitle)
                                    .build();    

    }



    


}
