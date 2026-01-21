package com.newsly.newsly.Registration;

import org.springframework.http.ResponseCookie;

import com.newsly.newsly.Registration.Model.AppRole;
import com.newsly.newsly.Registration.Model.AppUser;
import com.newsly.newsly.Registration.Model.JwtToken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder    
public 
class RegisterResponse {

    private AppUser appUser;

    private JwtToken refreshToken;
    private JwtToken accesToken;

    private ResponseCookie refreshCookie;

    private AppRole role;


    

}  
