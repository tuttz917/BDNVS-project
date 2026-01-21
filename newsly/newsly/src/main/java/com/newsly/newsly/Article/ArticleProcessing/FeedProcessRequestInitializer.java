package com.newsly.newsly.Article.ArticleProcessing;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Article.DataObjects.FeedData;
import com.newsly.newsly.Article.DataObjects.FeedProcessRequest;
import com.newsly.newsly.Article.DataObjects.RssFeedTags;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

@Component
class FeedProcessRequestInitializer implements IPipelineStep<FeedData,FeedProcessRequest>{

    @Override
    public FeedProcessRequest execute(FeedData feedData){

    String link= feedData.getLink();
    String source= feedData.getSource();
    RssFeedTags feedTags= feedData.getTags();

    return FeedProcessRequest.builder().link(link).source(source).tags(feedTags).build();




}

}