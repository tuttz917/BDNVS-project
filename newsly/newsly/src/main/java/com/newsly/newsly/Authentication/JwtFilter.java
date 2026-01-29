package com.newsly.newsly.Authentication;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.newsly.newsly.TextEditor.Models.JwtToken;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;


enum PublicEndpoints{

    REGISTER("/api/v1/auth"),
    LOGIN("/api/v1/register"),
    REFRESH("/api/v1/refresh"),
    ARTICLE("/api/v1/article"),
    GEOFEED("/api/v1/geoFeed");



    private final String path;

    PublicEndpoints(String path){

        this.path=path;
    }  

    public String getPath(){
        return this.path;
    }

    public static boolean contains(String endpointPath){

        for(PublicEndpoints publicEndpoint: PublicEndpoints.values()){
            if(endpointPath.equals(publicEndpoint.getPath())){
                return true;
            }
        }
        return false;

    } 

}



@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter  extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;



    public void doFilterInternal(HttpServletRequest request, HttpServletResponse resp, FilterChain filterChain) 
                        throws IOException, ServletException{
        
        log.info("Intram in filtrul de token");

        String path= request.getRequestURI();

        if(PublicEndpoints.contains(path)){

            log.info("ruta publica");

            filterChain.doFilter(request, resp);
            return;
        }

        String authHeader=request.getHeader("Authorization");

        if(authHeader!= null && authHeader.startsWith("Bearer")){

            String tokenText=authHeader.substring(7);
            JwtToken token=JwtToken.builder().token(tokenText).build();
            
            try{


                String username=jwtUtil.extractUsername(token);

            
                log.info("token valid");
            
            

                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(username,null, new ArrayList<>());
            

                SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                

            }   catch(JwtException exceptie){
                resp.setStatus(401);
                return;
            }
            
        }

    filterChain.doFilter(request, resp);

    }

}
