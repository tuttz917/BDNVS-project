package com.newsly.newsly.Article.Repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.newsly.newsly.Article.Models.DeletedArticle;

public interface DeletedArticleRepo extends MongoRepository<DeletedArticle,String> {
    

       

}


