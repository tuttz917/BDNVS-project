package com.newsly.newsly.TextEditor.Factchecking.Service;




import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.GeneralServices.SerpApiProvider;
import com.newsly.newsly.library.Pipelines.FunctionRegistry;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Configuration
public class SerpRegistryConfig {

    private SerpApiProvider serpApiProvider;

    
    @Bean("serpRegistry")
    public FunctionRegistry<String,List<String>> serpRegistry(){

        return FunctionRegistry.<String,List<String>>builder()    
                                                        .addFunction(serpApiProvider::getArticleLinks,"serp")
                                                        .build()
                                                        .applyBy((input)->{return "serp";});
                                                        
                                                        

    } 


}
