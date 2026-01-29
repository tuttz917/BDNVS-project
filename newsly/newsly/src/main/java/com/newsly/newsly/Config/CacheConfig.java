    package com.newsly.newsly.Config;

    import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;



import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;

    @Slf4j
    @Configuration
    @EnableCaching
    public class CacheConfig {


    @Bean("jedis")
public UnifiedJedis unifiedJedis() {
    
    HostAndPort address = new HostAndPort("redis-14054.crce218.eu-central-1-1.ec2.cloud.redislabs.com", 14054);
    DefaultJedisClientConfig config = DefaultJedisClientConfig.builder()
            .password("PD2a1zk3RfYgdI2IEnOUx52RuMl68qzy")
            .build();
                

    UnifiedJedis jedis= new UnifiedJedis(address, config);


    return jedis;
}


        @Bean
        public CommandLineRunner run(@Qualifier("jedis")UnifiedJedis jedis){

            return (args)->{
            
            

    
            
            log.info("testing cache conexion");

            try{
            log.info("writting test");
            jedis.set("Test","Testam conexiunea");
            }catch(Exception e){
                log.info("writting test esuat");
            }
            log.info("Transmitere de date reusita");

            try{
                log.info("retrieval test");
                Object test= jedis.get("Test");
                if (test!= null){
                    log.info("retrieval reusit");
                }
            }catch(Exception e){
                log.info("eroare la citirea datelor");
            }

            };

        }



        
    }
