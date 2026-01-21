package com.newsly.newsly.TextEditor.Factchecking.Data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder 
@AllArgsConstructor
public class FactCheckSimilarityCheckResult{

    String value;
    String searchContent;
    List<String> links;

}
