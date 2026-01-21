package com.newsly.newsly.TextEditor.Factchecking.Service.Steps;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckRequest;
import com.newsly.newsly.library.Pipelines.IPipelineStep;

import lombok.Data;

@Component
public class FactCheckAiResponseParser implements IPipelineStep<FactCheckRequest,FactCheckRequest> {


    @Override
    public FactCheckRequest execute(FactCheckRequest request){

        JsonNode node= request.getResponseNode();

        ObjectMapper mapper= new ObjectMapper();

        try{

            FactCheckOpenaiResponse response= mapper.readValue(node.asText(),FactCheckOpenaiResponse.class);
            
            String checkValue= response.getFinalClassification();
            List<String> topSources= response.getTopSources();

            request.setCheckValue(checkValue);
            request.setSelectedLinks(topSources);

            return request;

        }catch(Exception e){
            log.info("eroare la deserializare");
            throw new RuntimeException();
        }

        

    }

    
}


@Data
class FactCheckOpenaiResponse{

    @JsonProperty("top_sources")
    private List<String> topSources;
    @JsonProperty("final_classification")
    private String finalClassification;


}