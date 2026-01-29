package com.newsly.newsly.Authentication;

import org.springframework.http.ResponseCookie;


import com.newsly.newsly.TextEditor.Models.AppUser;
import com.newsly.newsly.TextEditor.Models.JwtToken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class LoginResponse {

    AppUser user;
    

    JwtToken accesToken;
    JwtToken refreshToken;

    ResponseCookie refreshCookie;
    
}
