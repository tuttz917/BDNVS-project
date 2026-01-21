package com.newsly.newsly.Registration.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private Long userId;
    private String token;


    public JwtToken(String token){
        this.token=token;
    }

    

    
    
}



interface JwtTokenRepo extends  JpaRepository<JwtToken,Long>{}