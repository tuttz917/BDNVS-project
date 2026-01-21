package com.newsly.newsly.TextEditor.Repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.TextEditor.Models.FactCheckResponse;



public interface FactCheckResponseRepo extends JpaRepository<FactCheckResponse,String>,FactCheckResponseCustom {

        
}


interface FactCheckResponseCustom{
    
}






