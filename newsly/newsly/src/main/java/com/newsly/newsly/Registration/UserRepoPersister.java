package com.newsly.newsly.Registration;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.AppUser;
import com.newsly.newsly.Registration.Model.ClientProfile;
import com.newsly.newsly.Registration.Model.JwtToken;
import com.newsly.newsly.Registration.Repository.AppRoleRepo;
import com.newsly.newsly.Registration.Repository.ClientProfileRepo;
import com.newsly.newsly.Registration.Repository.JwtTokenRepo;
import com.newsly.newsly.Registration.Repository.UserRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component 
public class UserRepoPersister implements Consumer<RegisterResponse> {

    UserRepo repo;
    
    @SuppressWarnings("null")
    @Override
    public void accept (RegisterResponse  registerResponse ){


        AppUser appUser= registerResponse.getAppUser();

        AppUser user= this.repo.save(appUser);

        registerResponse.setAppUser(user);

    }

}


@AllArgsConstructor
@Component
class JwtTokenPersister implements Consumer<RegisterResponse >{

    JwtTokenRepo repo;


    @SuppressWarnings("null")
    @Override 
    public void accept(RegisterResponse registerResponse){

        JwtToken token= registerResponse.getRefreshToken();

        this.repo.save( token);

    }

}



@AllArgsConstructor
@Component
class ClientProfilePersister implements Consumer<RegisterResponse> {

    @SuppressWarnings("unused")
    private ClientProfileRepo clientProfileRepo;
    private AppRoleRepo roleRepo;
    

    @Override
    public void  accept (RegisterResponse registerResponse){

        Long userId= registerResponse.getAppUser()
                                        .getId();

        Long roleId= roleRepo.findByRole("client")
                                .getId();

        ClientProfile.builder()
                        .userId(userId)
                        .roleId(roleId);

    }



}

