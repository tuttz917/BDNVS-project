package com.newsly.newsly.library.Pipelines;



import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class  Action<T> implements IPipelineStep<T,T>{

    private Consumer<T> consumer;
    private Boolean isFatal=false;
    private String canHandle;

    private Action(ActionBuilder<T> actionBuilder){
        this.consumer=actionBuilder.consumer;
        this.isFatal=actionBuilder.isFatal;
    }

    public void accept(T input) throws Exception{

        this.consumer.accept(input);

    }


    public Boolean isFatal(){
        return this.isFatal;
    }


    @Override 
    public T execute(T input) {
        try{
         this.getConsumer().accept(input);
         return input;
        }catch(Exception e){
            if(this.isFatal){
                throw new RuntimeException("Fatal consumer failed");
            }

            return input;
        }
    }

    public  static <T> ActionBuilder<T> builder(){

        return new ActionBuilder<>()   ;

    }

    public static class ActionBuilder<T>{

        private Consumer<T> consumer;
        private Boolean isFatal= false;

        public  Action<T> build(){

            return new Action<>(this);

        }

        public ActionBuilder<T> consumer(Consumer<T> consumer){
            this.consumer= consumer;
            return this;

        }



        

        public ActionBuilder<T> isFatal(){
            this.isFatal=true;
            return this;
        }



    }

}
