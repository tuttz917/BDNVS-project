package com.newsly.newsly.Authentication;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import javax.crypto.spec.GCMParameterSpec;

@Component 
public class EncryptionService {

    private static final  String ALGORITHM= "AES/GCM/NoPadding";
    private static final int IV_SIZE= 12;
    private static final int TAG_SIZE= 128;

    private final SecretKeySpec key;

    public EncryptionService(String key){

        this.key= new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"AES");

    }

    public String encypt(String data) throws Exception{

        byte[] iv=new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        Cipher cipher= Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, key,new GCMParameterSpec(TAG_SIZE, iv));

        byte[] encypted= cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        byte[] combined= new byte[encypted.length + iv.length ];

        System.arraycopy(iv, 0, combined,0, iv.length);
        System.arraycopy(encypted,0, combined, iv.length, encypted.length);

        String encryptedData= Base64.getEncoder().encodeToString(combined);

        return encryptedData;

    }


    public String decrypt(String data) throws Exception{

        byte[] encrypted= Base64.getDecoder().decode(data);

        byte[] iv= new byte[IV_SIZE];
        System.arraycopy(encrypted,0, iv, 0, iv.length);

        byte[] cipheredData=new byte[encrypted.length-iv.length];
        System.arraycopy(encrypted, iv.length,cipheredData, 0,cipheredData.length);

        Cipher cipher= Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_SIZE, iv));

        byte[] decryptedData= cipher.doFinal(cipheredData);

        String decryptedDataText= new String(decryptedData,StandardCharsets.UTF_8);

        return decryptedDataText;


    }


}
