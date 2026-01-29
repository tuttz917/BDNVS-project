package com.newsly.newsly.TextEditor.RedisRepo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.Factchecking.Data.FactCheckSimilarityCheckResult;

import com.newsly.newsly.TextEditor.Models.FactCheckResponse;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

@Slf4j
@Component

public class RedisFactCheckRepo {


    private UnifiedJedis jedis;


    public RedisFactCheckRepo(@Qualifier("jedis") UnifiedJedis jedis){
        this.jedis=jedis;
    }




public void save(FactCheckResponse response) {
    String baseKey = "factcheck:" + response.getId();
    String linksKey= baseKey+ ":links";


    ByteBuffer bb = ByteBuffer.allocate(response.getEmbedding().length * 4);
    bb.order(ByteOrder.LITTLE_ENDIAN);
    for (float f : response.getEmbedding()) {
        bb.putFloat(f);
    }
    byte[] embeddingBytes = bb.array();


    Map<byte[], byte[]> binaryFields = new HashMap<>();
    
    binaryFields.put("content".getBytes(), response.getContent().getBytes());
    binaryFields.put("value".getBytes(), response.getValue().getBytes());
    binaryFields.put("embedding".getBytes(), embeddingBytes);

    List<String> links= response.getLinks();

    List<byte[]> partialencoded= links.stream().map(link->link.getBytes()).collect(Collectors.toList());

    byte[][] encodedList= partialencoded.toArray(new byte[0][]);

    jedis.hset(baseKey.getBytes(), binaryFields);
    jedis.rpush(linksKey.getBytes(),encodedList);
    
    jedis.expire(baseKey, 2*60*60);
    jedis.expire(linksKey, 2*60*60);
}


public void saveInQueue(FactCheckResponse response){

    ObjectMapper mapper= new ObjectMapper();
    String key="sync_list:factcheck";

    try{
    String jsonString=mapper.writeValueAsString(response);

    jedis.lpush(key,jsonString);

    }catch(Exception e){
        log.info("eroare la serializare");
        throw new RuntimeException();
        
    }

}




public FactCheckSimilarityCheckResult findClosest(float[] embeddings) {
    
    ByteBuffer bb = ByteBuffer.allocate(embeddings.length * 4);

    bb.order(ByteOrder.LITTLE_ENDIAN);

    for (float f : embeddings) {
        bb.putFloat(f);
    }
    byte[] queryVector = bb.array();

   String queryExpression = "*=>[KNN 1 @embedding $vector AS vector_score]";
    
    Query q = new Query(queryExpression)
            .addParam("vector", queryVector)
            .dialect(2) 
            .returnFields("content", "value", "vector_score");

    log.info("Cautam cel mai apropiat vecin in indexul idx:factcheck");

    try {
        SearchResult result = jedis.ftSearch("idx:factcheck", q);

        if (result == null || result.getTotalResults() == 0) {
            log.info("Niciun rezultat gasit in Redis.");
            return null;
        }

        Document doc = result.getDocuments().get(0);

        String id= doc.getId();

        String linksId= id+":links";

        List<String> links= jedis.lrange(linksId, 0, -1);
        
        String score = doc.getString("vector_score");
        log.info("Distanta gasita: {}", score);

    

        return FactCheckSimilarityCheckResult.builder()
                .searchContent(doc.getString("content"))
                .value(doc.getString("value"))
                .links(links)
                .build();

    } catch (Exception e) {
        log.error("Eroare critica la FT.SEARCH: {}", e.getMessage());
        return null;
    }
}

}
