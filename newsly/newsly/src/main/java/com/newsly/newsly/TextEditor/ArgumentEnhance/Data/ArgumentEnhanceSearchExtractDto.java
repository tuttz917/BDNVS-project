package com.newsly.newsly.TextEditor.ArgumentEnhance.Data;

import java.util.List;

import com.newsly.Utils.Pair;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArgumentEnhanceSearchExtractDto{

    String context;
    Pair<Integer,Integer> targetBounds;
    List<SerpArticleDto> articles;

}
