package com.newsly.newsly.TextEditor.Factchecking.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SerpArticleDto{

    @JsonProperty("url")
    String link;
    String content;

}