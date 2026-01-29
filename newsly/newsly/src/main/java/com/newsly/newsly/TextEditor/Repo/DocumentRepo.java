package com.newsly.newsly.TextEditor.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.TextEditor.Models.Document;

public interface DocumentRepo extends JpaRepository<Document,String>{

    List<Document> findAllByUserId(Long userId);

}
    

