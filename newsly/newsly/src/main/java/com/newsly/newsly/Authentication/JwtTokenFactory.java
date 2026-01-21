package com.newsly.newsly.Authentication;

import com.newsly.newsly.Registration.Model.JwtToken;

public  class JwtTokenFactory {


    public static JwtToken getToken(String token){
        return new JwtToken(token);
    }
    
}
