package com.newsly.newsly.Registration;




public record RegisterRequestInputDto(String firstName, String lastName, String email, String userName, String password,String confirmPassword,String role)  {
}
