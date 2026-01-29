package com.newsly.newsly.Authentication;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Repo.UserRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component 
public class UsernamePredicate implements Predicate<LoginRequest>{

    private UserRepo repo;

    @Override 
    public boolean  test( LoginRequest request){

        log.info("verificare username");

        boolean result=repo.existsByUsername(request.getUsername());

        if(result==false){
            log.warn("nu exista un cont cu acest username");
        }

        return result;
}
}