package com.example.doan.service;


import com.example.doan.dtos.AuthDto;
import com.example.doan.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AuthDto getUser(String email);


    UserEntity getUserById(Long userId);

    void updateUserById(Long userId, UserEntity userEntity);

    AuthDto updateUserByIdWithImage(Long userId, AuthDto authDto);
}
