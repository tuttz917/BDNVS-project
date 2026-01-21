package com.newsly.newsly.Registration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.Registration.Model.ClientProfile;

public interface ClientProfileRepo extends  JpaRepository<ClientProfile,Long>,ClientProfileCustom{}


interface ClientProfileCustom{}


class ClientProfileRepoImpl implements ClientProfileCustom{}