package com.example.doan.service;

import com.example.doan.dtos.AuthDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    AuthDto createUser(AuthDto authDto);

    AuthDto resetPassword(AuthDto authDto);
}
