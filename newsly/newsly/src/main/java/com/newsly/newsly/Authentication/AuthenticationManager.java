package com.newsly.newsly.Authentication;

import java.util.List;
import java.util.function.Consumer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



import lombok.AllArgsConstructor;



@AllArgsConstructor
@Component
public class AuthenticationManager implements Consumer<LoginResponse> {



    @Override
    public void accept(LoginResponse loginResponse) {

        

        String username= loginResponse.getUser().getUsername();
    

        

        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken( username,null);

        SecurityContextHolder.getContext()
                    .setAuthentication(authenticationToken);

    }  

}
