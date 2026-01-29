package com.newsly.newsly.Article.Repo;









import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;


import com.newsly.newsly.Article.Models.Article;



public interface ArticleRepo extends MongoRepository<Article,String>, ArticleRepoCustom
{


    boolean existsByArticleURL(String url);

    boolean existsByTitle(String title);

    Article getArticleById(String id);




                                }


interface ArticleRepoCustom{



}


class ArticleRepoImpl implements ArticleRepoCustom{

    MongoTemplate mongoTemplate;


    

    }





