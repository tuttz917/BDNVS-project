package com.newsly.newsly.Registration;


import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Repo.UserRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class EmailDuplicatePredicate  implements Predicate<RegisterRequest>{

    
    private UserRepo repo;

   

    @Override
    public boolean test(RegisterRequest registerRequest){
    
        log.info("validare email");

        boolean result= !repo.existsByEmail(registerRequest.email());

        return result;

    }

  

}