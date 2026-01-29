package com.newsly.newsly.GeneralServices;

import java.net.URI;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.Utils.HttpUtil;
import com.newsly.Utils.JsonUtil;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@AllArgsConstructor
public class OpenAiService {
    

    public JsonNode  applyCompletion(String prompt, Object request){

        log.info("openai service");


        try{

        ObjectMapper mapper= new ObjectMapper();
            
        

        String mappedData= mapper.writeValueAsString(request);
        

        OpenaiRequest payload= OpenaiRequest.builder()
                                    .model("gpt-4o-mini")
                                    .addMessage("system", prompt)
                                    .addMessage("user", mappedData)
                                    .build();

        
                
        String json= JsonUtil.safeToJson(payload); 

        

        HttpRequest httpRequest= HttpRequest.newBuilder()
                            .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                            .header("Content-type","application/json")
                            .header("Authorization", "Bearer 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")
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();
        
        
        HttpResponse<String> response= HttpUtil.safeSend(httpRequest);

        

        try{
        
        JsonNode node= mapper.readTree(response.body());

        log.info(node.toPrettyString());

        JsonNode contentNode= node.get("choices").get(0).get("message").get("content");

        return contentNode;

        

        }catch(Exception e){
            log.info("eroare la deserializare");
            throw new RuntimeException();
        }
        }catch(JsonProcessingException e){
            log.info("json processing error");
            throw new RuntimeException();
        }
        


    }


    
}


@Data
@Builder
class OpenaiMessage{

    private String role;
    private String content;

}


@Data
class OpenaiRequest{

    private String model;
    private List<OpenaiMessage> messages= new ArrayList<>();


    private OpenaiRequest(OpenaiRequestBuilder builder){
        this.model=builder.model;
        this.messages=builder.messages;
    }

    public static OpenaiRequestBuilder builder(){
        return new OpenaiRequestBuilder();
    }

    @NoArgsConstructor
    public static class OpenaiRequestBuilder{

        private String model;
        private List<OpenaiMessage> messages= new ArrayList<>();



        public OpenaiRequest build(){
            return new OpenaiRequest(this);
        }

        public OpenaiRequestBuilder model(String model){
            this.model=model;

            return this;
        }

        public OpenaiRequestBuilder addMessage(OpenaiMessage message){

            this.messages.add(message);
            return this;

        }

        public OpenaiRequestBuilder addMessage(String role, String content){

            OpenaiMessage message= OpenaiMessage.builder().role(role).content(content).build();

            this.messages.add(message);

            return this; 

        }

    }

}
