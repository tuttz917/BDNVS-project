package com.newsly.newsly.TextEditor.SourceProviding.Data;

import java.util.List;

import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Builder
public class SourceRequestDto{

    private String content;
    private List<SerpArticleDto> articles; 

}