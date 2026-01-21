package com.newsly.newsly.Registration;



import java.util.function.Predicate;

import org.springframework.stereotype.Component;




import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@NoArgsConstructor
@Slf4j
@Component
 public class ConfirmPasswordPredicate  implements Predicate<RegisterRequestInputDto> {
    

    @Override
    public boolean test(RegisterRequestInputDto registerRequest){

        log.info("validare parola");

        boolean result=registerRequest.password().equals(registerRequest.confirmPassword());

        return result;
        
    }


}


