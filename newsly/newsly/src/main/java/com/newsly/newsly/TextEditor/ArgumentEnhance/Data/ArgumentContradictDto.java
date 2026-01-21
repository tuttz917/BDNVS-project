package com.newsly.newsly.TextEditor.ArgumentEnhance.Data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArgumentContradictDto{

    private String target;
    private String context;

}