package com.newsly.newsly.Registration;




public record RegisterRequest(String firstName, String lastName, String email, String userName, String password,String confirmPassword)  {
}
