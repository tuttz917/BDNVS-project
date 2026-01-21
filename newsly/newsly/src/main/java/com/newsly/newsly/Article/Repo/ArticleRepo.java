package com.newsly.newsly.Article.Repo;









import org.springframework.data.mongodb.repository.MongoRepository;


import com.newsly.newsly.Article.Models.Article;



public interface ArticleRepo extends MongoRepository<Article,String>
{


    boolean existsByArticleURL(String url);

    boolean existsByTitle(String title);

    Article getArticleById(String id);




                                }




