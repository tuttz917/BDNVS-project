package com.newsly.newsly.TextEditor.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.TextEditor.Models.ArgumentEnhanceResponse;

import lombok.AllArgsConstructor;

public interface ArgumentEnhanceResponseRepo extends JpaRepository<ArgumentEnhanceResponse,String> {
}

interface ArgumentEnhanceResponseCustom{}


@AllArgsConstructor
class ArgumentEnhanceResponseRepoImpl{}