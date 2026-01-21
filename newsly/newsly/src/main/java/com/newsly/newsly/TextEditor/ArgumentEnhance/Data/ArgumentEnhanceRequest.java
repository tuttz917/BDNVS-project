package com.newsly.newsly.TextEditor.ArgumentEnhance.Data;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsly.Utils.Pair;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArgumentEnhanceRequest {

    private String context;
    private String target;
    private Pair<Integer,Integer> targetBounds;
    private JsonNode searchResponseNode;
    private JsonNode responseNode;
    private String search;
    private List<SerpArticleDto> articles; 
    private String type;
    private String processedArgument;
    private List<String> responseSources;
    
}
