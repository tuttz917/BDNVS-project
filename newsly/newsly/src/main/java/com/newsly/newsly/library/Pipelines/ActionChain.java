package com.newsly.newsly.library.Pipelines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.transaction.support.TransactionTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Getter
@Slf4j
@RequiredArgsConstructor
public  class  ActionChain<T>  {

    private List<Action<T>> actionChain= new ArrayList<>();
    private Executor executor;
    private String transactionManagerQualifier;
    @Autowired
    Map<String, PlatformTransactionManager> managers;
  


    private ActionChain(ActionChainBuilder<T> builder){

        this.actionChain=builder.actionChain;
        this.executor=builder.executor;
        this.transactionManagerQualifier=builder.transactionManagerQualifier;

    }
    




    public void runChainAsync(T input){



    actionChain.stream()
                    .peek((consumer)->log.info("Executam consumer: " + consumer))
                    .forEach((consumer) -> CompletableFuture.runAsync(()->{
                    try{
                        consumer.getConsumer().accept(input);
                    }catch(Exception exception){
                        log.info("Consumerul: "+consumer + "a esuat "+ exception.getMessage());
                    }
                    },executor));



    }

    public void runChain(T input) {

        actionChain.stream()
                    .peek((action)->log.info("Executam actiunea "+ action))
                    .forEach((action)->{
                    try
                    {
                        action.accept(input);
                    }catch(Exception e){
                        log.info("consumerul a esuat");
                    }
                }
                    );
        


        

    }


    @SuppressWarnings("null")
    public void runTransactionalChain(T input){

        log.info("running transactional chain");

        TransactionTemplate template=new TransactionTemplate(managers.get(this.transactionManagerQualifier));

            template.executeWithoutResult( (status)->actionChain.stream()
                    .peek((action)->log.info("Executam actiunea "+ action))
                    .forEach((action)->{
                    try
                    {
                        action.accept(input);
                    }catch(Exception e){
                        log.info("consumerul a esuat");
                    }
                }
                    ));
        
    

    }






    public static <T> ActionChainBuilder<T> builder(){
        return new ActionChainBuilder<>();
    }

    public static class ActionChainBuilder<T>{

        private List<Action<T>> actionChain= new ArrayList<>();
        private Executor executor;
        private String transactionManagerQualifier;


        public ActionChain<T> build(){
            return new ActionChain<>(this);
        }

        public ActionChainBuilder<T> add(IPipelineStep<T,T> step){

            Consumer<T> enhancedConsumer= (input)->{step.execute(input);};

            Action<T> action=Action.<T>builder().consumer(enhancedConsumer)
                                .build();

            this.actionChain.add(action);
            return this;

        }

        public <O> ActionChainBuilder<T> addThen(IPipelineStep<T,O> step){

            Consumer<T> enhancedConsumer= (input)->{step.execute(input);};

            Action<T> action=Action.<T>builder().consumer(enhancedConsumer)
                                .build();

            this.actionChain.add(action);
            return this;

        }

        public ActionChainBuilder<T> add(Function<T,T>step){

            Consumer<T> enhancedConsumer=(input)->{step.apply(input);};

            Action<T> action= Action.<T>builder().consumer(enhancedConsumer)
                                                .build();
            
            this.actionChain.add(action);
            return this;

        }

        public ActionChainBuilder<T> add(Action<T> action){

            this.actionChain.add(action);
            return this;

        }


        public ActionChainBuilder<T> add(Consumer<T> consumer){


        Action<T> action= Action.<T>builder()
                            .consumer(consumer)
                            .build();

            this.actionChain.add(action);
            return this;

        }

        public ActionChainBuilder<T> add(ActionChain<T> actionChain){

            this.actionChain.addAll(actionChain.getActionChain());
            return this;

        }


        public ActionChainBuilder<T> add(Pipeline<T,?> pipeline){

            Consumer<T> consumer= (input)->{pipeline.getPipeline().execute(input);};
            
            Action<T> action=Action.<T>builder().consumer(consumer).build();

            this.actionChain.add(action);

            return this;

        }

        public ActionChainBuilder<T> transactionManager(String beanName){

            this.transactionManagerQualifier=beanName;
            return this;

        }

        

        public ActionChainBuilder<T> executor(Executor executor){
            this.executor=executor;
            return this;
        }



        

        

    }

}
