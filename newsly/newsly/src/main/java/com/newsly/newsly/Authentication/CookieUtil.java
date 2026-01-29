package com.newsly.newsly.Authentication;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;



import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class CookieUtil{


        @SuppressWarnings("null")
        public ResponseCookie createRefreshCookie(String jwtToken,Duration duration){

            return ResponseCookie.from("refreshToken", jwtToken)
                                .httpOnly(true)
                                .secure(false)//in productie il punem true
                                .maxAge( duration)
                                .path("/")
                                .sameSite("Lax")
                                .build();

        }

}