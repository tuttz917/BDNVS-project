package com.newsly.newsly.TextEditor.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Document {

    @Id
    private String documentId;
    private String name;
    private String content;
    private Long userId;
    
}