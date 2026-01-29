package com.newsly.newsly.TextEditor.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.newsly.newsly.TextEditor.Models.JwtToken;
import java.util.List;




public interface JwtTokenRepo extends JpaRepository<JwtToken,Long>, JwtTokenRepoCustom{

    void deleteByUserId(Long userId);
    JwtToken findByToken(String token);

}

interface JwtTokenRepoCustom {}


@Component
class JwtTokenRepoImpl implements JwtTokenRepoCustom{}