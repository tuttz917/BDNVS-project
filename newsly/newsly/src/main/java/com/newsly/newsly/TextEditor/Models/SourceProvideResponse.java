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

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SourceProvideResponse{


    @Id
    private String id;

    @Column(name="embedding", columnDefinition = "vector(384)")
    private float[] embedding;

    private String content;

    @ElementCollection
    @CollectionTable(

        name="source_links",
        joinColumns = @JoinColumn(name="sourceResponse_id")

    )
    List<String> links;

}


