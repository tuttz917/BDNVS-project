package com.newsly.newsly.library.Pipelines;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;




public class ConsumerRegistry<T > implements IPipelineStep<T,T>{
    

    private Map<String, IPipelineStep<T,T>> registry=  new HashMap<>();
    private Function<T,String> keySupplier;

    private ConsumerRegistry(ConsumerRegistryBuilder<T> builder){

        this.registry=builder.registry;

    }

    public IPipelineStep<T,T> findConsumer( T input) {

        String key= keySupplier.apply(input);


        for(Map.Entry<String,IPipelineStep<T,T>> entry : registry.entrySet()){

            if(entry.getKey().equals(key))
                    return entry.getValue();

        }

        throw new RuntimeException();

    }

    public ConsumerRegistry<T> applyBy(Function<T,String> keySupplier){

            this.keySupplier=keySupplier;
            return this;

    }


    @Override 
    public T execute(T input) {

        this.findConsumer(input)
            .execute(input);

        return input;

    }


    public static <T> ConsumerRegistryBuilder<T> builder(){

        return new ConsumerRegistryBuilder<>(); 

    }

    public static class ConsumerRegistryBuilder<T >{

        private Map<String,IPipelineStep<T,T>> registry= new HashMap<>();


        private ConsumerRegistryBuilder(){}


        public ConsumerRegistry<T> build(){

            return new ConsumerRegistry<>(this);

        }


        public ConsumerRegistryBuilder<T> addConsumer(Consumer<T> consumer,String key){

            IPipelineStep<T,T> pipelineStep = (input) ->{consumer.accept(input); return input;};

            this.registry.put(key, pipelineStep);

            return this;

        } 

        public ConsumerRegistryBuilder<T> addConsumer(IPipelineStep<T,T> pipeline, String key) {


            registry.put(key, pipeline);
            
            return this;

        }

    }

}
