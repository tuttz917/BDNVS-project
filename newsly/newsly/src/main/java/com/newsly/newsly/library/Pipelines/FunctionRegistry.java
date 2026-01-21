package com.newsly.newsly.library.Pipelines;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public  class FunctionRegistry<T , O > {

    private  Map<String,List<IPipelineStep<T,O>>> registry= new HashMap<>();
    private IPipelineStep<T,String> keySupplier;


    private FunctionRegistry(FunctionRegistryBuilder<T,O> builder){

        this.registry=builder.registry;

    }


    private List<IPipelineStep<T,O>> getFunction( T input)  {

        String key= keySupplier.execute(input);

        log.info("extracting suitable steps");

        for(Map.Entry<String,List<IPipelineStep<T,O>>> entry : registry.entrySet()){

            if(entry.getKey().equals(key) && !entry.getValue().isEmpty()){
                return entry.getValue();
            }
            

        }

        throw new RuntimeException("Function not found");

    }


    public O applyFirst( T input) {


        List<IPipelineStep<T,O>> potentialSteps= this.getFunction(input);

        for (IPipelineStep<T,O> step: potentialSteps){

            try{
                log.info("trying step: "+step);
                O result= step.execute(input);

                log.info("step realizat cu succes");

                return result;

            }catch(Exception e){
                log.info("failed step: "+step);
                continue;
            }

        }

        log.info("nu am gasit nici o functie potrivita care sa functioneze");
        throw new RuntimeException();
            
    
    }

    public List<O> applyAll(T input) throws Exception{

        List<IPipelineStep<T,O>> potentialSteps= this.getFunction(input);

        List<O> processedObjects= new ArrayList<>();

        for(IPipelineStep<T,O> pipelineStep: potentialSteps){

            O processed= pipelineStep.execute(input);
            processedObjects.add(processed);
        }

        return processedObjects;

    }




    public FunctionRegistry<T,O> applyBy(IPipelineStep<T,String>keySupplier){

        this.keySupplier=keySupplier;
        return this;

    }




    public static <T,O> FunctionRegistryBuilder<T,O> builder(){

        return new FunctionRegistryBuilder<>();

    } 


    public static class FunctionRegistryBuilder<T , O>{

        private Map<String,List<IPipelineStep<T,O>>> registry=  new HashMap<>();

        public FunctionRegistry<T,O> build(){
            return new FunctionRegistry<>(this);
        }
        
        public FunctionRegistryBuilder<T,O> addFunction(@NonNull IPipelineStep<T,O> function, String key){

            this.registry.computeIfAbsent(key, k->new ArrayList<>())
                        .add(function)  ;
            return this;

        }
        
        @SuppressWarnings("unchecked")
        public FunctionRegistryBuilder<T,O> addConsumer(@NonNull Consumer<T> consumer, String key){

            IPipelineStep<T,O> convertedStep= (input)->{consumer.accept(input);return (O) input;};

            this.registry.computeIfAbsent(key, k->new ArrayList<>())
                        .add(convertedStep);
                
            return this;

        }

    

        public FunctionRegistryBuilder<T,O> addFunction(@NonNull Pipeline<T,O> pipeline, String registry){


            IPipelineStep<T,O> pipelineSteps= pipeline.getPipeline();

            this.registry.computeIfAbsent(registry, k->new ArrayList<>())
                        .add(pipelineSteps);
            
            return this;

        }

    }
}
