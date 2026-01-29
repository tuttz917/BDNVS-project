// package com.newsly.newsly.Article;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
// import org.springframework.context.annotation.Bean;
// import org.springframework.data.transaction.ChainedTransactionManager;
// import org.springframework.transaction.PlatformTransactionManager;

// import com.newsly.newsly.Article.Models.Article;
// import com.newsly.newsly.Article.Models.DeletedArticle;
// import com.newsly.newsly.Article.Repo.ArticleRepo;
// import com.newsly.newsly.Article.Repo.DeletedArticleRepo;
// import com.newsly.newsly.GeneralServices.LinkContentExtractor;
// import com.newsly.newsly.GeneralServices.ToxicityValidator;
// import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;
// import com.newsly.newsly.TextEditor.RedisRepo.RedisArticleRepo;
// import com.newsly.newsly.library.Pipelines.ActionChain;
// import com.newsly.newsly.library.Pipelines.IPipelineStep;

// import lombok.Builder;
// import lombok.Data;
// import redis.clients.jedis.UnifiedJedis;

// public class ToxicReportCheckPipelineConfig {
    
// }

// @Data
// class ToxicCheckRawRequest{

//     private String articleId;

// }


// @Data
// @Builder
// class ToxicCheckRequest{

//     private Article article;
//     private String content;
//     private List<String> splitContent;
//     private DeletedArticle deletedArticleDto;

// }


// class ToxicCheckRequestFactory implements IPipelineStep<ToxicCheckRawRequest, ToxicCheckRequest>{

//     private ArticleRepo repo;

//     @Override
//     public ToxicCheckRequest execute(ToxicCheckRawRequest rawRequest){


//         String articleId= rawRequest.getArticleId();

//         Article article= repo.getArticleById(articleId);

//         return ToxicCheckRequest.builder().article(article).build();


//     } 

// }

// class ToxicCheckContentExtractor implements IPipelineStep<ToxicCheckRequest,ToxicCheckRequest>{

//     private LinkContentExtractor contentExtractor;

//     @Override
//     public ToxicCheckRequest execute(ToxicCheckRequest request){

//         List<String> links= new ArrayList<>();

//         String url= request.getArticle().getArticleURL();

//         links.add(url);

//         List<SerpArticleDto> dtos= contentExtractor.extractContentFrom(links);

//         String content= dtos.get(0).getContent();

//         request.setContent(content);

//         return request;


//     }


// } 

// class ToxicCheckContentTransformer implements IPipelineStep<ToxicCheckRequest,ToxicCheckRequest>{


//     @Override 
//     public ToxicCheckRequest execute(ToxicCheckRequest request){

//         String content=request.getContent();

//         List<String> contentSplits= new ArrayList<>();

//         while(content.length()>200){

            

//                 String section= content.substring(0,200);
//                 content= content.substring(200); 
//                 contentSplits.add(section);


//         }

//         contentSplits.add(content);

//         request.setSplitContent(contentSplits);

//         return request;

//     }

// }


// class ToxicCheckRequestValidation implements IPipelineStep<ToxicCheckRequest,ToxicCheckRequest>{

//     private ToxicityValidator validator;

//     @Override
//     public ToxicCheckRequest execute(ToxicCheckRequest request){

//         List<String> splits= request.getSplitContent();
//         Boolean toxicFlag= false;

//         for(String split:splits){

//             if(!validator.test(split)){

//                 toxicFlag=true;
//                 break;
//             }

//         }

//         if (toxicFlag=true){

//             log.info("raportul este unul valid");
//             return request;
//         }

//         else{

//             log.info("raportul este unul invalid- abort");
//             throw new RuntimeException();

//         }

//     }

// }

// class ToxicCheckDeletedArticleDtoEnhancer implements IPipelineStep<ToxicCheckRequest,ToxicCheckRequest>{


//     @Override
//     public ToxicCheckRequest execute(ToxicCheckRequest request){

//         Article article= request.getArticle();

//         DeletedArticle deletedArticleDto= DeletedArticle.builder().articleUrl(article.getArticleURL()).build();

//         request.setDeletedArticleDto(deletedArticleDto);

//         return request;



//     }

// }


// class ToxicCheckActionChainConfig{

//     ArticleRepo  articleRepo;
//     DeletedArticleRepo deletedArticleRepo;
//     RedisArticleRepo cache;

//     @Bean("toxicCheckActionChainConfig")
//     public ActionChain<ToxicCheckRequest> build(){

//         return ActionChain.<ToxicCheckRequest>builder().add((request)->{articleRepo.delete(request.getArticle());})
//                                                         .add((request)->{deletedArticleRepo.save(request.getDeletedArticleDto());})
//                                                         .transactionManager("articleDbTransactionManager")
//                                                         .build();
//     }

//     @Bean("toxicCheckTm")
//     private PlatformTransactionManager buildTransactionManagerChain(@Qualifier("")){

//         return new ChainedTransactionManager(null)

//     }


// }