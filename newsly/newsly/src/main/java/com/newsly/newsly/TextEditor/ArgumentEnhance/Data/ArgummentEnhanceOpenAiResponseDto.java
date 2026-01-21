package com.newsly.newsly.TextEditor.ArgumentEnhance.Data;

import java.util.List;

import lombok.Data;

@Data
public class ArgummentEnhanceOpenAiResponseDto{

    private String target;
    private List<String> sources;

}