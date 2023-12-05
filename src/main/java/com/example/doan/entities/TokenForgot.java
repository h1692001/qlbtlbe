package com.example.doan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenForgot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String token;
    private Instant expired;
}
