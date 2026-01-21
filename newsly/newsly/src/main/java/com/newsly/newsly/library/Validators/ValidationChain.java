package com.newsly.newsly.library.Validators;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.extern.slf4j.Slf4j;



@Slf4j
public  class ValidationChain<T> implements IPipelineStep<T,T>  {

    private List<Validator<T>> validatorList;
    private String exceptionMessage;
    private String overrideExceptionMessage;

    

    private ValidationChain(ValidationChainBuilder<T> validatorChainBuilder){
        this.validatorList=validatorChainBuilder.validatorList;
    }

    public T validate(T input) throws Exception{

        log.info("suntem in functia de validare a validationchainului");

        for(Validator<T> validator: validatorList){

            

            Boolean result= validator.test(input);

            if(result && validator.isKey()){
                
                log.info("validatorul cheie a trecut");
                return input;

            }

            if(!result){

                if(!validator.isFatal()){
                    log.info("validator non-fatal nu a trecut");
                }
                else{

                    this.exceptionMessage=validator.getExceptionMessage();
        
                    if(this.overrideExceptionMessage!=null){

                        throw new Exception(this.overrideExceptionMessage);

                    }
                    log.info("urmeaza sa arunca eroare");
                    throw new Exception(this.exceptionMessage);
                    
                }
                


            }

        }

        return input;

    }



    @Override 
    public T execute(T input) {
        
        try{
            return this.validate(input);
        }catch(Exception e){
            log.info("Chainul de validare nu a trecut");
            throw new RuntimeException();
        }
        
    
    } 
    
    
        public static <T> ValidationChainBuilder<T> builder(){
            return   new ValidationChainBuilder<>();
        }
    
    public static class ValidationChainBuilder<T>{

        private List<Validator<T>> validatorList= new ArrayList<>();




        public ValidationChain<T> build(){
            return new ValidationChain<>(this);
        }


        public ValidationChainBuilder<T> then(Validator<T> validator){

            this.validatorList.add(validator);

            return this;

        }



        public ValidationChainBuilder<T> then(Predicate<T> predicate){

            Validator<T> validator= Validator.<T>builder()
                        .predicate(predicate)
                        .build();
            
            this.validatorList.add(validator);
            return this;

        }

    }

}


