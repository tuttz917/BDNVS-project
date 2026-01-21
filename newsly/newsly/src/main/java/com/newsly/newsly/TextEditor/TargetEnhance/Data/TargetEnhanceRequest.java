package com.newsly.newsly.TextEditor.TargetEnhance.Data;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsly.Utils.Pair;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TargetEnhanceRequest{

    private String context;
    private String target;
    private JsonNode responseNode;
    private String enhancedContent;
    private Pair<Integer,Integer> targetBounds;

}
