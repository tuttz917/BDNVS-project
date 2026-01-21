package com.newsly.newsly.TextEditor.Factchecking.Data;

import java.util.List;

import lombok.Builder;

@Builder
public record FactCheckRequestDto(String content, List<SerpArticleDto> articles){}