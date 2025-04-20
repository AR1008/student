package com.university.attendance.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        // For testing purposes, allowing simple login without database access
        if ("admin".equals(username) && "password".equals(password)) {
            return new UsernamePasswordAuthenticationToken(
                username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        } else if ("faculty1".equals(username) && "password".equals(password)) {
            return new UsernamePasswordAuthenticationToken(
                username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_FACULTY")));
        } else if ("student1".equals(username) && "password".equals(password)) {
            return new UsernamePasswordAuthenticationToken(
                username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));
        }
        
        throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}