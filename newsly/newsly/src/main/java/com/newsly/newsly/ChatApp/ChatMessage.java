package com.newsly.newsly.ChatApp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ChatMessage {

    private Long senderId;
    private String content;

}
