package com.newsly.newsly.TextEditor.RedisRepo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.ScanCursor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.Models.Document;
import com.newsly.newsly.TextEditor.Models.FactCheckResponse;
import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;
import com.newsly.newsly.TextEditor.Repo.DocumentRepo;
import com.newsly.newsly.TextEditor.Repo.FactCheckResponseRepo;
import com.newsly.newsly.TextEditor.Repo.SourceProvideResponseRepo;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

@Service
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class SyncService {

    UnifiedJedis jedis;
    FactCheckResponseRepo factCheckRepo;
    SourceProvideResponseRepo sourceRepo;
    RedisSourceRepo sourceCache;
    RedisFactCheckRepo factCheckCache;
    DocumentRepo documentRepo;


    @Scheduled(fixedDelay = 30000)
    public void syncDatabase(){

        if(isOn()){

            syncFactChecks();
            syncSources();
            syncDocuments();

            log.info("sync done");

        }else{

            log.info("database is off");

        }

    }

    private boolean isOn(){

        try{

            factCheckRepo.count();
            return true;

        }catch(Exception e){
            log.info("baza de date inactive");
            return false; 
        }

    }

    private void syncFactChecks(){

        String syncKey= "sync_list:factcheck";
        ObjectMapper mapper= new ObjectMapper();
        String tempKey= "sync_list:factcheck:processing:"+System.currentTimeMillis();

        try{
            jedis.rename(syncKey,tempKey);
        }catch(Exception e){
            log.info("nimic de sync la factcheck");
            return;
        }

        try{
        List<String> nodes= jedis.lrange(tempKey,0,-1);


        List<FactCheckResponse> responses= new ArrayList<>();


        for(String node:nodes){


            try{

                FactCheckResponse model= mapper.readValue(node,FactCheckResponse.class);
                responses.add(model);

            }catch(Exception e){
                log.info("eroare la deserializare");
            }

            

        }

        factCheckRepo.saveAll(responses);
        jedis.del(tempKey);}
        catch(Exception e){

            log.info("sync ul a esuat, mutam la loc datele");

            jedis.lpush(syncKey,jedis.lrange(tempKey,0,-1).toArray(new String[0]));
            jedis.del(tempKey);
    
        }
        
    }

    private void syncSources(){

        String syncKey= "sync_list:source";
        ObjectMapper mapper= new ObjectMapper();
        String tempKey= "sync_list:source:processing:"+ System.currentTimeMillis();

        try{
        jedis.rename(syncKey,tempKey);
        }catch(Exception e){
            log.info("nimic de sync la surse");
            return;
        }


        try{
        List<String> nodes= jedis.lrange(tempKey, 0, -1);

        List<SourceProvideResponse> responses= new ArrayList<>();


        for(String node:nodes){
            
            

                SourceProvideResponse model= mapper.readValue(node,SourceProvideResponse.class);
                responses.add(model);


        }

        sourceRepo.saveAll(responses);
        jedis.del(tempKey);

        }catch(Exception e){

            log.info("sync-ul a esuat");
            jedis.lpush(syncKey,jedis.lrange(tempKey,0,-1).toArray(new String[0]));
            jedis.del(tempKey);
        }

    
}
    @Transactional
    private void syncDocuments(){
        
        String cursor= "0";
        String pattern= "document:*";

        ScanParams params= new ScanParams().match(pattern).count(100);

        do{

            ScanResult<String> scanResult= jedis.scan(cursor,params);
            for(String key: scanResult.getResult()){

                String updatedContent= jedis.get(key);
                String id= key.substring("document:".length());
                try{
                String syncKey="sync:document:"+ id;
                if(jedis.get(syncKey).equals("false")){

                Document document= documentRepo.findById(id).get();
                document.setContent(updatedContent);
                documentRepo.save(document);

                jedis.set(syncKey,"true");

                }                    
                
                
                }catch(Exception e){
                    log.info("documentul nu mai exista in baza de date");
                
                }
            

                cursor= scanResult.getCursor();

            }

        }while(!cursor.equals("0"));

    }

}