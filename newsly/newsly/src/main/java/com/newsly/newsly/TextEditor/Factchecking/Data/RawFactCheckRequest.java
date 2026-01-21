package com.newsly.newsly.TextEditor.Factchecking.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class RawFactCheckRequest {

    private String content;
    
}
