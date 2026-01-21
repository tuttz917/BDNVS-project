package com.newsly.newsly.TextEditor.SourceProviding.Data;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SourceSimilarityCheckResult{

    String content;
    List<String> links;

}