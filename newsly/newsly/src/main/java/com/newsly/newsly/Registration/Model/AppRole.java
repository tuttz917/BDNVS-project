package com.newsly.newsly.Registration.Model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



@Entity
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String role;


    public AppRole(){}

    public AppRole(String role){
        this.role=role;
    }




    public Long getId(){
        return this.id;
    }

    public String getRole(){
        return this.role;
    }

    public void setRole(String role){
        this.role=role;
    }

}


