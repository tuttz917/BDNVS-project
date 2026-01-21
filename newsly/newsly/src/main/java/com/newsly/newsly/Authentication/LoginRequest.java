package com.newsly.newsly.Authentication;



import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginRequest {

    public String username;
    public String password;
    public String role;



}
