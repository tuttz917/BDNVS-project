package com.newsly.newsly.TextEditor.Factchecking.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Data
@AllArgsConstructor
public class FactCheckResponseDto{

    @JsonProperty("top_sources")
    private List<String> links;
    @JsonProperty("final_classification")
    private String value;


    public String getValue(){
        return this.value;
    }

}