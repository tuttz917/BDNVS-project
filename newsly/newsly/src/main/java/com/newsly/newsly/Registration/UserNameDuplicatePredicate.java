package com.newsly.newsly.Registration;



import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Repository.UserRepo;



import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public 
class UserNameDuplicatePredicate  implements Predicate<RegisterRequestInputDto>{

    private UserRepo repo;

    public boolean test(RegisterRequestInputDto registerRequest){

        log.info("se verifica unicitatea username-ului");

        boolean result= !repo.existsByUsername(registerRequest.userName());

   
        return result;
}
  

}
