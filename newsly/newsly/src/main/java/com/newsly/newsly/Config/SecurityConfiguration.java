package com.newsly.newsly.Config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.newsly.newsly.Authentication.JwtFilter;

import lombok.RequiredArgsConstructor;


@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
    .cors(cors-> cors.configurationSource(corsConfigurationSource()))
        .httpBasic(basic -> basic.disable())
        .formLogin(login -> login.disable())
                .csrf(csrf -> csrf.disable())
             // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/chat/**").permitAll()
                .requestMatchers("/app/chat").permitAll()
                .requestMatchers("/ChatClient.html").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/articles").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/articles/search").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/login").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/fact-check").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/source-provide").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/grammar-check").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/target-enhance").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/argument-add").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/argument-contradict").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/article").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/v1/geofeed").permitAll()
                .anyRequest().authenticated())

            

                .build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration= new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:5173", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("POST","GET","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

}
