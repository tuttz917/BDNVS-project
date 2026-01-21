package com.newsly.newsly.TextEditor.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;

public interface SourceProvideResponseRepo extends JpaRepository<SourceProvideResponse,String>,SourceProvideResponeRepoCustom {
    
}


interface SourceProvideResponeRepoCustom{}
