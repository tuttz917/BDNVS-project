package com.newsly.newsly.Registration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.JwtToken;



public interface JwtTokenRepo extends JpaRepository<JwtToken,Long>, JwtTokenRepoCustom{}

interface JwtTokenRepoCustom {}


@Component
class JwtTokenRepoImpl implements JwtTokenRepoCustom{}