package com.newsly.newsly.Authentication;

import java.io.IOException;
import java.util.List;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import com.newsly.newsly.Registration.Model.AppRole;
import com.newsly.newsly.Registration.Model.JwtToken;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;


enum PublicEndpoints{

    REGISTER("/api/v1/register"),
    LOGIN("/api/v1/login"),
    LOAD("/api/v1/articles"),
    SEARCH("/api/v1/search");
   

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
public class JwtFilter  {

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
            JwtToken token= JwtTokenFactory.getToken(tokenText);
            
            try{

                String username=jwtUtil.extractUsername(token);

                AppRole role= jwtUtil.extractRole(token);

                List<GrantedAuthority> authorities=  List.of(new SimpleGrantedAuthority("ROLE_"+role.getRole()));

                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(username,null,authorities);
               

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
