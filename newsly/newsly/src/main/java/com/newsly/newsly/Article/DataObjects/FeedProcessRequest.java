package com.newsly.newsly.Article.DataObjects;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FeedProcessRequest {

    private String link;
    private String source;
    private RssFeedTags tags;
    private String feedContent;
    

}
