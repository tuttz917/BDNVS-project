package com.newsly.newsly.Article.ArticleProcessing.ArticleValidation;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;


import com.newsly.newsly.Article.Models.Article;
import com.newsly.newsly.Article.Repo.ArticleRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component 
public class DuplicateTitle implements Predicate<Article> {

    ArticleRepo articleRepo;

    @Override 
    public boolean test(Article articleDto){

        log.info("verificam duplicitatea de titlu");

        String title= articleDto.getTitle();

        if(articleRepo.existsByTitle(title)){

            return false;

        }

        return true;

    }
    
}
