package com.newsly.newsly.Authentication;




import java.util.Date;

import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.AppRole;
import com.newsly.newsly.Registration.Model.JwtToken;

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
    
    
    public JwtToken generateToken(String username, int expiration, String role){

        String tokenText= Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        
                return new JwtToken(tokenText);

    }

    private <T> T extractClaim(JwtToken token, Function<Claims,T> claimResolver){

        Claims claims= Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token.getToken())
                        .getBody();
        
        return claimResolver.apply(claims);

    }

    
    public AppRole extractRole(JwtToken token){

        String roleString= extractClaim(token, claims->{

            return claims.get("role", String.class);

        });

        return new AppRole(roleString);

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






