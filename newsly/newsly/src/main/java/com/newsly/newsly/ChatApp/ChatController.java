package com.newsly.newsly.ChatApp;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Controller
public class ChatController {

    public ChatController(){
        log.info("a fost instantiat");
    }


    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String handleMessage(@Payload String message){

        log.info("Am primit message "+ message );
        return message;
    }


    
}


