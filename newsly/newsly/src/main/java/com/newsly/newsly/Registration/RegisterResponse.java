package com.newsly.newsly.Registration;

import org.springframework.http.ResponseCookie;

import com.newsly.newsly.TextEditor.Models.AppUser;
import com.newsly.newsly.TextEditor.Models.JwtToken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Builder    
public 
class RegisterResponse {

    private AppUser appUser;

    private JwtToken refreshToken;
    private JwtToken accesToken;

    private ResponseCookie refreshCookie;



    

}  
