package com.newsly.newsly.Registration;


import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Repository.UserRepo;



import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class EmailDuplicatePredicate  implements Predicate<RegisterRequestInputDto>{

    
    private UserRepo repo;

   

    @Override
    public boolean test(RegisterRequestInputDto registerRequest){
    
        log.info("validare email");

        boolean result= !repo.existsByEmail(registerRequest.email());

        return result;

    }

  

}