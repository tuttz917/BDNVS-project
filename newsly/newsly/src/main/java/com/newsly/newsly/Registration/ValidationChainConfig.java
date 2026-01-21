package com.newsly.newsly.Registration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newsly.newsly.library.Validators.ValidationChain;
import com.newsly.newsly.library.Validators.Validator;


@Configuration
public class ValidationChainConfig {

    @Bean("regReqValidationChain")
    ValidationChain<RegisterRequestInputDto> registerRequestValidationChain(Validator<RegisterRequestInputDto> confirmPassValidator
                                                                        , Validator<RegisterRequestInputDto> emailDuplValidator
                                                                        , Validator<RegisterRequestInputDto> usernameDuplValidator){

            return ValidationChain.<RegisterRequestInputDto>builder()
                                                        .then(confirmPassValidator)
                                                        .then(emailDuplValidator)
                                                        .then(usernameDuplValidator)
                                                        .build();
                                                        


    }


    @Bean("confirmPassValidator")
    Validator<RegisterRequestInputDto> confirmPasswordValidator(ConfirmPasswordPredicate passwordPredicate){

        return Validator.<RegisterRequestInputDto>builder()
                                                .predicate(passwordPredicate)
                                                .build();
        
    }


    @Bean("emailDuplValidator")
    Validator<RegisterRequestInputDto> emailDuplicateValidator(EmailDuplicatePredicate emailPredicate){

        return Validator.<RegisterRequestInputDto>builder()
                                                .predicate(emailPredicate)
                                                .build();

    }

    @Bean("usernameDuplValidator")
    Validator<RegisterRequestInputDto> usernameDuplicatePredicate(UserNameDuplicatePredicate usernamePredicate){

        return Validator.<RegisterRequestInputDto>builder() 
                                                .predicate(usernamePredicate)
                                                .build();

    }


    

}
