package com.newsly.newsly.TextEditor.SourceProviding.Data;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsly.newsly.TextEditor.Factchecking.Data.SerpArticleDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SourceProvideRequest{

    String rawContent;
    String processedContent;
    List<String> links;
    List<SerpArticleDto> articles;
    JsonNode responseNode; 
    List<String> selectedLinks;
    float[] embedding;
    SourceSimilarityCheckResult searchResponse;
    

}