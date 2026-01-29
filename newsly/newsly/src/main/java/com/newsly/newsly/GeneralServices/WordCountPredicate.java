package com.newsly.newsly.GeneralServices;




import org.springframework.stereotype.Component;



import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@NoArgsConstructor
public class WordCountPredicate  {

     
    public boolean test(String content, int maxWordCountLimit, int minWordCountLimit) {

        log.info("wordcount predicate");

        String[] wordsArray= content.split(" ");

        if (wordsArray.length>maxWordCountLimit && wordsArray.length<minWordCountLimit){
            return false;
        }

        return true;

    }

}


