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
public class DuplicateUrl implements Predicate<Article> {

    ArticleRepo articleRepo;

    @Override 
    public boolean test(Article article){


        log.info("verificam duplicitatea de url");

        String url= article.getArticleURL();

        if(articleRepo.existsByArticleURL(url)){

            return false;

        }

        return true;

    }

}
