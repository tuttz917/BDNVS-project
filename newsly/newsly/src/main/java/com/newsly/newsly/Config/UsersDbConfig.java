package com.newsly.newsly.Config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;



@Configuration
@EnableJpaRepositories(
    basePackages = "com.newsly.newsly.Registration.Repository",
    entityManagerFactoryRef= "userEmf",
    transactionManagerRef= "userTm"
)
@EnableTransactionManagement
public class UsersDbConfig{

    @Bean
    DataSource dataSource(){

        HikariDataSource dataSource= DataSourceBuilder.create()
                                        .type(HikariDataSource.class)
                                        .url("jdbc:postgresql://localhost:5432/articles")
                                        .username("postgres")
                                        .password("tutza")
                                        .build();

        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);

        return dataSource;
    }

    
    @Bean(name="userEmf")
    LocalContainerEntityManagerFactoryBean userContainerEntityManagerFactory(){

        HibernateJpaVendorAdapter jpaVendorAdapter= new HibernateJpaVendorAdapter();

        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(false);

        
        LocalContainerEntityManagerFactoryBean factory= new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(this.dataSource());
        factory.setPackagesToScan("com.newsly.newsly.Registration.Model");
        factory.setJpaVendorAdapter(jpaVendorAdapter);
    

        return factory;

    }

    
    @Bean(name="userTm")
    PlatformTransactionManager transactionManager(@Qualifier("userEmf") EntityManagerFactory factory){

        JpaTransactionManager jpaTransactionManager= new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(factory);

        return jpaTransactionManager;

    }


}
