package com.newsly.newsly.Authentication;

import org.springframework.http.ResponseCookie;

import com.newsly.newsly.Registration.Model.AppRole;
import com.newsly.newsly.Registration.Model.AppUser;
import com.newsly.newsly.Registration.Model.JwtToken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class LoginResponse {

    AppUser user;
    AppRole role;

    JwtToken accesToken;
    JwtToken refreshToken;

    ResponseCookie refreshCookie;
    
}
