package com.newsly.newsly.TextEditor.Factchecking.Data;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FactCheckRequest {

    
    private String language;
    private String rawRequest;
    private String processedContent;
    private List<SerpArticleDto> articles;
    private List<String> links;
    private float[] embedding;
    private JsonNode responseNode;
    private FactCheckSimilarityCheckResult similarityCheckResult;
    private String nliClassificationLabel;
    private String initialValue;
    private List<String> selectedLinks;
    private String checkValue;
    
}
