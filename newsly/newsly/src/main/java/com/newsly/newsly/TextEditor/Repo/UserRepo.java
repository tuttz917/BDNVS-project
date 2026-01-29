    package com.newsly.newsly.TextEditor.Repo;

import java.util.List;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.newsly.newsly.TextEditor.Models.AppUser;

import lombok.AllArgsConstructor;


public interface UserRepo extends JpaRepository<AppUser,Long>, 
JpaSpecificationExecutor<AppUser>,UserRepoCustom{


        boolean existsByEmail(String email);
        boolean existsById(@SuppressWarnings("null") Long id);
        boolean existsByUsername(String userName);
        AppUser findByEmail(String email);
        @SuppressWarnings("null")
        List<AppUser> findAll(Specification<AppUser>specification);
        AppUser  findByUsername(String username);
        
    }

interface UserRepoCustom{}

@AllArgsConstructor
class UserRepoImpl implements UserRepoCustom{

   

}


