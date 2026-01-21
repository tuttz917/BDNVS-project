package com.newsly.newsly.Registration.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsly.newsly.Registration.Model.AppRole;

public interface AppRoleRepo extends JpaRepository<AppRole,Long>,AppRoleRepoCustom{

    AppRole findByRole(String role);

}


interface AppRoleRepoCustom {}


class AppRoleRepoImpl implements AppRoleRepoCustom{}