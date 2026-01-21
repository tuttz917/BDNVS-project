package com.newsly.newsly.TextEditor.GeneralServices;

import java.text.Normalizer;


import org.springframework.stereotype.Component;



import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor
public class TextNormalizer  {

    
    public String normalizeText(String content){

        log.info("text normalizer");

        

        String normalizedContent= Normalizer.normalize(content, Normalizer.Form.NFKC);

        normalizedContent= normalizedContent.toLowerCase();

        normalizedContent = normalizedContent.replaceAll(
                "[^\\p{L}\\p{N}\\s?!.,:%€$-]",
                " "
        );

        normalizedContent = normalizedContent.replaceAll("\\s+", " ").trim();

        
        return content;


    }
    
}
