package com.newsly.newsly.library.Pipelines;



import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@FunctionalInterface
public interface IPipelineStep<T ,O >{

    Logger log=LoggerFactory.getLogger(IPipelineStep.class);


    O execute(T input);

    
    default List<O> executeMany(List<T> inputs){

        return inputs.stream()
                    .map(input-> {return execute(input);})
                    .collect(Collectors.toList());

    }

    default <NewO > IPipelineStep<T,NewO> then(IPipelineStep<O,NewO> nextStep){
        log.info("are loc un .then");
        return (input)->{
            
            if(input==null){
                throw new RuntimeException();
            }

            O  result= this.execute(input);
            return nextStep.execute(result);
        };
    }

    default IPipelineStep<T,O> thenConsumer(Consumer<O> consumer){

        return(input)->{

            O result= this.execute(input);

            consumer.accept(result);

            return result;
        };

    }

    
}





