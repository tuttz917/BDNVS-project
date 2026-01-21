package com.newsly.newsly.TextEditor.GrammarCheck.Data;



import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrammarCheckRequest{

    private String rawContent; 
    private String processedContent;
    private JsonNode responseNode;

}
