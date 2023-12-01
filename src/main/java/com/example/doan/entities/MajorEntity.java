package com.example.doan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "major", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<UserEntity> users;

    @OneToMany(mappedBy = "major")
    private List<ClassV> classVList;

    @ManyToOne
    private Faculties faculties;
}
