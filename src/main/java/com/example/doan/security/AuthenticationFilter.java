package com.example.doan.security;

import com.example.doan.SpringApplicationContext;
import com.example.doan.dtos.AuthDto;
import com.example.doan.dtos.AuthLoginRequest;
import com.example.doan.entities.UserEntity;
import com.example.doan.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Key getSigningKey() {
        byte[] keyBytes = SecurityContants.getTokenSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            AuthLoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), AuthLoginRequest.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails userDetails) {
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        authentication.getCredentials(),
                        authorities
                );
            }
            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String userName = ((User) auth.getPrincipal()).getUsername();
        UserService userService=(UserService) SpringApplicationContext.getBean("userServiceImpl");
        AuthDto authDto=userService.getUser(userName);
        String token = Jwts.builder().setSubject(userName).setExpiration(new Date(System.currentTimeMillis() + SecurityContants.EXPIRATION_TIME)).signWith(getSigningKey()).compact();
        JSONObject responseData = new JSONObject();
        responseData.put("email", authDto.getEmail());
        responseData.put("userId", authDto.getUserId());
        responseData.put("avatar", authDto.getAvatar());
        responseData.put("message", "Login succesfully");
        responseData.put("accessToken",SecurityContants.TOKEN_PREFIX+token);
        responseData.put("status", 0);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(responseData.toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        JSONObject responseData = new JSONObject();
        responseData.put("message", "Incorrect email or password");
        responseData.put("status", 1);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseData.toString());
    }



}
