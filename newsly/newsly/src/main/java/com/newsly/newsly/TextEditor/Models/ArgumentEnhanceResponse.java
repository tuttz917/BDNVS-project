package com.newsly.newsly.TextEditor.Models;

import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
public class ArgumentEnhanceResponse{


    @Id
    private String id;


    @Column(name="search_embedding", columnDefinition = "vector(384)")
    private float[] searchEmbedding; 
    private String target;

    @ElementCollection
    @CollectionTable(

        name= "argument_links",
        joinColumns = @JoinColumn(name="argument_id")

    )
    List<String> links;

}




