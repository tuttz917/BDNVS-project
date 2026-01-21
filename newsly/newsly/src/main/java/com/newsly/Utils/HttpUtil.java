package com.newsly.Utils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil{

    public static HttpResponse<String> safeSend(HttpRequest request){

        HttpClient client= HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

        try{
        HttpResponse<String> response= client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
        }catch(InterruptedException exception){
            log.info("eroare de intrerupere");
            throw new RuntimeException("eroare de intrerupere");   
        }catch(IOException exception){
            log.info("eroare de IO");
            throw new RuntimeException("eroare de IO");
        }



    }

}
