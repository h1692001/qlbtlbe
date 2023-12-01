package com.example.doan.security;

import com.example.doan.exception.ApiException;
import com.example.doan.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthService authService;
    public AuthorizationFilter(AuthenticationManager authenticationManager, AuthService authService) {
        super(authenticationManager);
        this.authService=authService;
    }

    public AuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint,  AuthService authService) {
        super(authenticationManager, authenticationEntryPoint);
        this.authService=authService;
    }

    private Key getSigningKey() {
        byte[] keyBytes = SecurityContants.getTokenSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException{
        String header=req.getHeader(SecurityContants.HEADER_STRING);
        String tokenParam=req.getParameter("authorization");
        if(tokenParam!=null){
            UsernamePasswordAuthenticationToken authenticationToken=getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(req,res);
        }else{
            if(header==null ||!header.startsWith(SecurityContants.TOKEN_PREFIX)){
                chain.doFilter(req,res);
                res.setContentType("application/json");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            res.getOutputStream().println(ErrorMessage.TOKEN_IS_NULL.getErrorMessage()+ ErrorMessage.TOKEN_IS_NULL.getStatus());
                return ;
            }
            UsernamePasswordAuthenticationToken authenticationToken=getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(req,res);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String tokenHeader = req.getHeader(SecurityContants.HEADER_STRING);
        if (tokenHeader != null) {
            tokenHeader = tokenHeader.replace(SecurityContants.TOKEN_PREFIX, "");
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(tokenHeader).getBody();
            String user = claims.getSubject();
            if (user != null) {
                UserDetails userDetails = this.authService.loadUserByUsername(user);
                return new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Token is invalid");
            }
        }

        String tokenParam = req.getParameter("authorization");
        if (tokenParam != null) {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(tokenParam).getBody();
            String user = claims.getSubject();
            if (user != null) {
                UserDetails userDetails = this.authService.loadUserByUsername(user);
                return new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Token is invalid");
            }
        }
        return null;
    }

    private List<GrantedAuthority> extractAuthorities(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        if (roles != null) {
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
