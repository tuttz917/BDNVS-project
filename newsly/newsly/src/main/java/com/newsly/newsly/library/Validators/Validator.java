package com.newsly.newsly.library.Validators;



import java.util.function.Predicate;

import com.newsly.newsly.library.Pipelines.IPipelineStep;


import lombok.Getter;

import lombok.NonNull;





@Getter
public  class Validator<T> implements IPipelineStep<T,T> {

    private Predicate<T> predicate;
    private Boolean isKey= false;
    private Boolean isFatal= true;
    private String exceptionMessage;


    private Validator(ValidatorBuilder<T> builder){

      this.predicate=builder.predicate;
      this.isKey=builder.isKey;
      this.isFatal=builder.isFatal;
      this.exceptionMessage=builder.exceptionMessage;

    }

  public T validate(T obj) {

    if(this.isKey && !this.isFatal){
    throw new RuntimeException("Un validator nu poate fi cheie si non-fatal in acelasi timp");
    }

    if(!this.predicate.test(obj)){

    throw new RuntimeException(this.exceptionMessage);

    }

    return obj;
  
  }

  public boolean test(T obj){
    return this.predicate.test(obj);
  }


  public Boolean isKey(){
    return this.isKey;
  }

  public Boolean isFatal(){
    return this.isFatal;
  }

  public static <T> ValidatorBuilder<T> builder(){
      return new ValidatorBuilder<>();
    }

  @Override
  public T execute(T input) {
    return this.validate(input) ;
  }


  public static class ValidatorBuilder<T>{

    private Predicate<T> predicate;
    private Boolean isKey= false;
    private Boolean isFatal= true;
    private String exceptionMessage; 



    public Validator<T> build(){

      return new Validator<>(this);

    }

    public ValidatorBuilder<T> predicate(@NonNull Predicate<T> predicate){

      this.predicate=predicate;
      return this;
      
    }



    public ValidatorBuilder<T> isKey(){
      this.isKey=true;
      return this;

    }

    public ValidatorBuilder<T> nonFatal(){

      this.isFatal=false;
      return this;

    }

    public ValidatorBuilder<T> errorMessage(String message){

      this.exceptionMessage=message;
      return this;
    }



  } 

}