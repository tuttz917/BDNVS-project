package com.newsly.newsly.TextEditor.ArgumentEnhance.Data;

import java.util.List;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ArgumentEnhanceResponseDto{



    private String target;
    private List<String> sources;

}

