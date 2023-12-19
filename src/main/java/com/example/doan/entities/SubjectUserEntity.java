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
public class SubjectUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String role;
    private int submit;
    private Date submitedAt;
    private int status;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private SubjectEntity subject;

}
