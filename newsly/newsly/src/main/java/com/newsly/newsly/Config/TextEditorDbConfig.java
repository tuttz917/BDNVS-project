package com.newsly.newsly.Config;

import java.util.HashMap;
import java.util.Map;

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

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(

    basePackages = "com.newsly.newsly.TextEditor.Repo",
    entityManagerFactoryRef = "editorEmf",
    transactionManagerRef = "editorTm"

)
public class TextEditorDbConfig {
    

    
    @Bean(name="textEditorDb")
    public DataSource dataSource(){

        HikariDataSource dataSource= DataSourceBuilder.create()
                                    .type(HikariDataSource.class)
                                    .url("jdbc:postgresql://localhost:5434/mydb")
                                    .username("postgres")
                                    .password("mypass")
                                    .build();
        
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(2);

        dataSource.setInitializationFailTimeout(-1);

        return dataSource;
        
        
                    

    }

    @Bean(name="editorEmf")
    public LocalContainerEntityManagerFactoryBean editorContainerEntityManagerFactoryBean(){

        HibernateJpaVendorAdapter vendorAdapter= new HibernateJpaVendorAdapter();

        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);


        LocalContainerEntityManagerFactoryBean factory= new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.newsly.newsly.TextEditor.Models");


        Map<String, Object> props = new HashMap<>();
props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
factory.setJpaPropertyMap(props);


        return factory;

    }

    @Bean(name="editorTm")
    public PlatformTransactionManager editorTransactionManager(@Qualifier("editorEmf") EntityManagerFactory editorEmf){

        JpaTransactionManager transactionManager= new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(editorEmf);

        return transactionManager;

    }

}
