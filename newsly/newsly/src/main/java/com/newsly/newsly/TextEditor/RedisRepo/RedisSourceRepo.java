package com.newsly.newsly.TextEditor.RedisRepo;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsly.newsly.TextEditor.Models.SourceProvideResponse;
import com.newsly.newsly.TextEditor.SourceProviding.Data.SourceSimilarityCheckResult;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;

@Slf4j
@Component
public class RedisSourceRepo {

    UnifiedJedis jedis;

    public RedisSourceRepo(@Qualifier("jedis") UnifiedJedis jedis){
        this.jedis=jedis;
    }

    public void saveToQueue(SourceProvideResponse response){

        String key="sync_list:source";

        ObjectMapper mapper= new ObjectMapper();

        try{

            String json= mapper.writeValueAsString(response);

            jedis.lpush(key,json);

        }catch(Exception e){

            log.info("eroare la deserializare");
            throw new RuntimeException();

        }


    }


    public void save(SourceProvideResponse response){

        log.info(response.toString());

        String basekey= "source:"+ response.getId();
        String linksKey= basekey + ":links"; 

        Map<byte[],byte[]> binaryFields= new HashMap<>();

        ByteBuffer bb= ByteBuffer.allocate(response.getEmbedding().length*4);

        for(float embedding: response.getEmbedding()){

            bb.putFloat(embedding);

        }

        byte[] embeddings= bb.array();
        String[] array= response.getLinks().toArray(new String[0]);

        byte[][] links= Arrays.stream(array).map(string->string.getBytes()).toArray(byte[][]::new);

        binaryFields.put("embedding".getBytes(),embeddings);
        binaryFields.put("content".getBytes(),response.getContent().getBytes());

        jedis.hset(basekey.getBytes(),binaryFields);
        jedis.rpush(linksKey.getBytes(), links);
        

    }
    
    public SourceSimilarityCheckResult findClosest(float[] embeddings){

        log.info("check1");

        ByteBuffer bb= ByteBuffer.allocate(embeddings.length*4);

        for(float embedding: embeddings){

            bb.putFloat(embedding);

        }

        byte[] array= bb.array();

        String queryExpression = "@embedding:[VECTOR_RANGE 0.02 $vector]=>{$yield_distance_as: vector_score}";

        log.info("check2");

        Query query= new Query(queryExpression).addParam("vector",array).dialect(2).returnFields("content","vector_score");

        SearchResult rs= jedis.ftSearch("idx:sources",query);

        log.info(rs.toString());

        

        log.info("check10");
        if (rs == null ||rs.getTotalResults()==0 ){

            log.info("raspuns null sau gol");
            return null;

        }

        log.info("check5");

        Document doc= rs.getDocuments().get(0);

        String score= doc.getString("vector_score");

        log.info(score);

        String id= doc.getId();


        log.info(id);

        String linksKey= id+":links";



        log.info("check4");

        List<String> links= jedis.lrange(linksKey, 0, -1);

        log.info(links.toString());

        String content= doc.getString("content");

        return SourceSimilarityCheckResult.builder().content(content).links(links).build();

    }

    
}

