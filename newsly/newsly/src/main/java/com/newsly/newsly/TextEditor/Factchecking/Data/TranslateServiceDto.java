package com.newsly.newsly.TextEditor.Factchecking.Data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslateServiceDto {
    String content ;
    String targetLanguage;
}
