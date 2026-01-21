package com.newsly.newsly.TextEditor.Factchecking.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToxicityRaport {

    private double  toxicity;
    @JsonProperty("severe_toxicity")
    private double severeToxicity;
    private double obscene;
    private double threat;
    private double insult;
    @JsonProperty("identity_attack")
    private double identityAttack;

}