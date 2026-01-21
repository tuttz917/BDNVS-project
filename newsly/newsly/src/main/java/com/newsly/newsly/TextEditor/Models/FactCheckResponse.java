package com.newsly.newsly.TextEditor.Models;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity(name="factcheckresponse")
public class FactCheckResponse {
    
    @Id
    private String id;

    private String content;

    @Column(name="embedding", columnDefinition = "vector(384)")
    private float[] embedding;

    private String value;



    @ElementCollection
    @CollectionTable(

        name="factCheck_links",
        joinColumns = @JoinColumn(name="factCheckResponse_id")

    )
    private List<String> links;
}


