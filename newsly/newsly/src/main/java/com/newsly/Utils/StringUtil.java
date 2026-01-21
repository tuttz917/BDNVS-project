package com.newsly.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil{

    public static String toPgVector(float[] array){

        log.info("string util");

        StringBuilder sb= new StringBuilder();

        sb.append("[");

        for(int i=0;i<array.length-1;i++){

            String converted= String.valueOf(array[i]);
            sb.append(converted).append(",");


        }

        sb.append(array[array.length-1]).append("]");

        log.info(sb.toString());

        return sb.toString();

        

    }

}