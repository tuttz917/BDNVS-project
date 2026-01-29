package com.newsly.newsly.Authentication;




import java.util.Date;

import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;


import com.newsly.newsly.TextEditor.Models.JwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

@Component  
@AllArgsConstructor
public class JwtUtil{

    private final  SecretKey secretKey;

    public JwtUtil(String key){
        this.secretKey= new SecretKeySpec(key.getBytes(), "AES");
    }
    
    
    public JwtToken generateToken(String username, int expiration){

        String tokenText= Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        
                return new JwtToken(tokenText);

    }

    public JwtToken generateToken(String username, int expiration, Long userId){

        String tokenText= Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        
                return JwtToken.builder().token(tokenText).userId(userId).build();

    }


    public  String extractUsername(JwtToken token){

        String tokenText=token.getToken();

        Claims claims=Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(tokenText)
                    .getBody();
        
        return claims.getSubject();


    }

}






