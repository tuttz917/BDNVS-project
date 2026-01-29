package com.newsly.newsly.Registration;

import com.newsly.newsly.TextEditor.Models.AppUser;

public class AppUserFactory{

    public static AppUser getUser(String firstName, String lastName, String email, String userName, String password ){

        return new AppUser(firstName,lastName,email,userName,password);

    }


    public static AppUser getUserFromRegistration(RegisterRequest request){

        return getUser(request.firstName(),request.lastName(),request.email(),request.userName(),request.password());

    }

}
