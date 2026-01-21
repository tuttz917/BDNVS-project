package com.newsly.newsly.Authentication;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Repository.UserRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class PasswordPredicate implements Predicate<LoginRequest>{

    private UserRepo repo;

    @Override 
    public boolean test(LoginRequest request){

        log.info("verificare parola");

        boolean result=request.getPassword().
                        equals(repo.findByUsername(request.getUsername())
                        .getPassword());

        if(result==false){
            log.warn("parola nu este corecta");
        }

        return result;

    }

}
