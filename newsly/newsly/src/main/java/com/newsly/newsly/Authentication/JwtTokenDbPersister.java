package com.newsly.newsly.Authentication;

import java.util.function.Consumer;


import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.JwtToken;
import com.newsly.newsly.Registration.Repository.JwtTokenRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component 
public class JwtTokenDbPersister implements Consumer<LoginResponse> {

   
    private JwtTokenRepo repo;
    
    
    @SuppressWarnings("null")
    @Override 
    public void accept(LoginResponse loginRequest){

        JwtToken refreshToken= loginRequest.getRefreshToken();
        
    
        this.repo.save(refreshToken);        
    
    }

}


