package com.example.doan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String className;


    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "classV")
    private List<ClassVUser> classVUsers;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "classV")
    private List<SubjectEntity> subjects;

    private int status;

    @ManyToOne
    private MajorEntity major;

    private Date createdAt;

}
