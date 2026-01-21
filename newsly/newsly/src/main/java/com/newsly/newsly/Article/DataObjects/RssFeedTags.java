package com.newsly.newsly.Article.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RssFeedTags{

    String ItemTag;
    String TitleTag;
    String DescriptionTag;
    String ImageTag;
    String linkTag;
    String pubDateTag;

}