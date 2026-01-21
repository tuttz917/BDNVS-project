package com.newsly.newsly.TextEditor.ArgumentEnhance.Data;



import java.util.List;

import com.newsly.Utils.Pair;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArgumentEnhanceRequestDto {
    
    private String context;
    private Pair<Integer,Integer> targetLimits;
    private List<SerpArticleDto> articleDtos;

}
