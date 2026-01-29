package com.newsly.newsly.Registration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.library.Validators.ValidationChain;
import com.newsly.newsly.library.Validators.Validator;


@Configuration
public class ValidationChainConfig {

    @Bean("regReqValidationChain")
    ValidationChain<RegisterRequest> registerRequestValidationChain(Validator<RegisterRequest> confirmPassValidator
                                                                        , Validator<RegisterRequest> emailDuplValidator
                                                                        , Validator<RegisterRequest> usernameDuplValidator){

            return ValidationChain.<RegisterRequest>builder()
                                                        .then(confirmPassValidator)
                                                        .then(emailDuplValidator)
                                                        .then(usernameDuplValidator)
                                                        .build();
                                                        


    }


    @Bean("confirmPassValidator")
    Validator<RegisterRequest> confirmPasswordValidator(ConfirmPasswordPredicate passwordPredicate){

        return Validator.<RegisterRequest>builder()
                                                .predicate(passwordPredicate)
                                                .build();
        
    }


    @Bean("emailDuplValidator")
    Validator<RegisterRequest> emailDuplicateValidator(EmailDuplicatePredicate emailPredicate){

        return Validator.<RegisterRequest>builder()
                                                .predicate(emailPredicate)
                                                .build();

    }

    @Bean("usernameDuplValidator")
    Validator<RegisterRequest> usernameDuplicatePredicate(UserNameDuplicatePredicate usernamePredicate){

        return Validator.<RegisterRequest>builder() 
                                                .predicate(usernamePredicate)
                                                .build();

    }


    

}
