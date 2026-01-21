package com.newsly.newsly.Registration;

import com.newsly.newsly.Registration.Model.AppUser;

public class AppUserFactory{

    public static AppUser getUser(String firstName, String lastName, String email, String userName, String password ){

        return new AppUser(firstName,lastName,email,userName,password);

    }


    public static AppUser getUserFromRegistration(RegisterRequestInputDto request){

        return getUser(request.firstName(),request.lastName(),request.email(),request.userName(),request.password());

    }

}
