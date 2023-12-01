package com.example.doan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Faculties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "faculties",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<MajorEntity> majors;

    @OneToMany(mappedBy = "faculty",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<UserEntity> user;
}
