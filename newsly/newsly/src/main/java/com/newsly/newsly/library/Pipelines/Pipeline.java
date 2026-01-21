package com.newsly.newsly.library.Pipelines;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;







import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import lombok.extern.slf4j.Slf4j;








@Setter
@Slf4j
@RequiredArgsConstructor
@Getter
public  class Pipeline<T, O>  {

    protected  IPipelineStep<T, O> pipeline;
    
    Executor defaultExecutor;

  

    private Runnable busRunHook;



    private Pipeline(IPipelineStep<T,O> pipeline, Executor executor){

        this.pipeline=pipeline;
        this.defaultExecutor=executor;
        

    }

    public Pipeline(Pipeline<T,O> pipeline){
        this.pipeline=pipeline.pipeline;
        this.defaultExecutor=pipeline.defaultExecutor;
    }




    public Result<T, O> run(T input) {

        try {

            if(input!=null){
                log.info("suntem in run one");
                log.info(input.toString());
                O resultObject = pipeline.execute(input);
                log.info(pipeline.toString());
                log.info(resultObject.toString());

            return Result.success(resultObject);
            }
            else {
                log.info("obiect null detectat");
                throw new NullPointerException("obiectul dat in pipeline este null");
            }

        } catch (Exception e) {
            log.info("exceptie in pipeline");
            //e.printStackTrace();
            return Result.fail(input, e.getMessage());

        }

    }

    public Result<List<T>, List<O>> runMany(List<T> inputList) {
    List<O> results = inputList.stream()
        .map(input -> {
            
                return this.run(input);

        })
        .filter((result)->result.getValue()!=false)
        .map(Result::getResultData)
        .collect(Collectors.toList());

    return Result.success(inputList, results);
}


        
    public Result<List<T>, List<O>> runAsyncMany(List<T> inputList) {

        

        List<O> results = inputList.stream()
                .filter(input-> input!=null)
                .map(input -> {
                    
                    return CompletableFuture.supplyAsync(() -> this.run(input),
                            defaultExecutor);
                })
                .map(CompletableFuture::join)
                .filter(resultObj -> resultObj.getValue() != false)
                .map(input -> input.getResultData())
                .collect(Collectors.toList());

                return Result.success(inputList,results);
    }



    public static <T> PipelineBuilder<T,T> builder(){
        return new PipelineBuilder<>((input)-> {return input;},null);
    }

    public static class PipelineBuilder<T,O>{

        private IPipelineStep<T,O> pipelineSteps;
        private Executor executor;

      

       

        private PipelineBuilder(IPipelineStep<T,O> pipelineStep, Executor executor){
            this.pipelineSteps=pipelineStep;
            this.executor=executor;
        }

        public Pipeline<T,O> build(){

            return new Pipeline<>(pipelineSteps,executor);

        }


        public <NewO> PipelineBuilder<T,NewO> then(IPipelineStep<O,NewO> pipelineStep){

            IPipelineStep<T,NewO> newPipelineStep= this.pipelineSteps.then(pipelineStep);
            
            return new PipelineBuilder<>(newPipelineStep, executor);
        }

        public <NewO> PipelineBuilder<T,NewO> then(Pipeline<O,NewO> pipeline){

            IPipelineStep<O,NewO> pipelineSteps= pipeline.getPipeline();
            
            IPipelineStep<T,NewO> newPipelineStep= this.pipelineSteps.then(pipelineSteps);
            
            return new PipelineBuilder<>(newPipelineStep, executor);
        }


        public  PipelineBuilder<T,O> thenConsume(Consumer<O> pipelineStep){

            IPipelineStep<O,O> adaptedPipelineStep= (input)->{ pipelineStep.accept(input); return input;};

            IPipelineStep<T,O> newPipelineStep= this.pipelineSteps.then(adaptedPipelineStep);
            
            return new PipelineBuilder<>(newPipelineStep, executor);
        }




        public PipelineBuilder<T,O> then(Predicate<O> predicate){

            IPipelineStep<O,O> adaptedPipelineStep= (input)->{

                if(predicate.test(input)){

                    return input;

                }

                return null;

            };

            IPipelineStep<T,O> newPipelineStep= this.pipelineSteps.then(adaptedPipelineStep);

            return new PipelineBuilder<>(newPipelineStep, executor);

        }


        public <NewO> PipelineBuilder<T,NewO> then(Supplier<NewO> supplier){

            IPipelineStep<O, NewO> adaptedPipelineStep= (ignoredInput)->{return supplier.get();};

            IPipelineStep<T,NewO> newPipelineStep= this.pipelineSteps.then(adaptedPipelineStep);

            return new PipelineBuilder<>(newPipelineStep, executor);

        }



        public PipelineBuilder<T,O> executor(Executor executor){
            this.executor=executor;
            return this;
        }

      
        

    }


}




