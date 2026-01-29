package com.newsly.newsly.TextEditor.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.newsly.newsly.TextEditor.Models.AppUser;
import com.newsly.newsly.TextEditor.Models.Document;
import com.newsly.newsly.TextEditor.Repo.DocumentRepo;
import com.newsly.newsly.TextEditor.Repo.UserRepo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.UnifiedJedis;
import org.springframework.web.bind.annotation.RequestParam;






@Slf4j
@RestController 
public class EditorController2 {

    @Autowired
    UserRepo userRepo;

    @Autowired 
    DocumentRepo repo;
    
    @Autowired
    UnifiedJedis jedis;

    @GetMapping("/api/v1/documents")
    public List<Document> getMethodName() {
        
        log.info("suntem aici");

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        String username= (String)auth.getPrincipal();

        log.info(username);

        AppUser user= userRepo.findByUsername(username);

        Long id= user.getId();

        log.info(Long.toString(id));

        List<Document> documents= repo.findAllByUserId(id);

        return documents;

    }
    

    @GetMapping("/api/v1/document/{id}")
    @ResponseBody
    public String loadContent(@PathVariable("id") String docId) {

        String key = "document:" + docId;
        String syncKey= "sync:"+key;


        String content= jedis.get(key);

        if(content==null){

            Document doc= repo.findById(docId).get();
            content= doc.getContent();
            jedis.set(key, content);
            jedis.set(syncKey,"true");
            
            jedis.expire(key,60*30);
            jedis.expire(syncKey,60*30);

        }

        
        return content;
    }

    
    @MessageMapping("/update/{docId}")
    public void receiveUpdate(@DestinationVariable String docId, String content) {

        log.info("are loc update");


        String key = "document:" + docId;
        String syncKey= "sync:document:"+ docId;

        jedis.set(key, content);
        jedis.set(syncKey,"false");

        jedis.expire(key,60*30);
        jedis.expire(syncKey,60*30);
    
    }

    @PostMapping("/api/v1/save-document/{docId}")
    public void getMethodName2(@PathVariable String docId) {
        
        String key= "document:"+docId;
        String content= jedis.get(key);

        Document doc= repo.findById(docId).get();

        doc.setContent(content);
        jedis.set("sync:"+key,"true");

        repo.save(doc);

    }
    



    @PostMapping("/api/v1/create-document")
    public Document getMethodName(@RequestBody DocumentDto documentDto ) {
        
        String content= "Spor la lucru";

        String id= UUID.randomUUID().toString();

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        String username= (String)auth.getPrincipal();

        log.info(username);

        AppUser user= userRepo.findByUsername(username);

        Long userId= user.getId();

        log.info(Long.toString(userId));

        log.info(documentDto.getDocumentName());

        Document document= Document.builder().name(documentDto.getDocumentName()).content(content).documentId(id).userId(userId).build();

        String key= "document:"+id;

        jedis.set(key,content);

        repo.save(document);

        log.info(document.toString());

        return document;

    }

    @DeleteMapping("/api/v1/delete-document/{id}")
    public ResponseEntity deleteDocument(@PathVariable String id){

        String cacheKey= "document:"+ id;

        jedis.del(cacheKey);
        jedis.del(cacheKey);
        jedis.del("sync:"+cacheKey);

        repo.deleteById(id);

        return ResponseEntity.ok().build();

    }
    

}


@NoArgsConstructor
@Data
class DocumentDto{

    String documentName;

} 

class CachedDocumentDto{

    private String documentId;
    private String content;

}