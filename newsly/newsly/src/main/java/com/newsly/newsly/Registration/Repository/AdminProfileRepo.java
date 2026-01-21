package com.newsly.newsly.Registration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.Registration.Model.AdminProfile;

public interface AdminProfileRepo extends  JpaRepository<AdminProfile,Long>,AdminProfileRepoCustom{}

interface AdminProfileRepoCustom{}

class AdminProfileRepoImpl implements AdminProfileRepoCustom{}
