package com.newsly.newsly.Article.ArticleProcessing.ArticleValidation;








public class StringUtil {


        public static String normalize(String string){
    return string.trim()
                .toLowerCase()
                .replaceAll("[<>*&]", "");
}

}








