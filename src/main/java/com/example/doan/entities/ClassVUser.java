package com.example.doan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ClassVUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String role;
    private int submit;
    private Date submitedAt;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ClassV classV;

}
