package com.example.doan.repository;

import com.example.doan.entities.TokenForgot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenForgotRepository extends JpaRepository<TokenForgot,Long> {
    TokenForgot findByToken(String token);
}
