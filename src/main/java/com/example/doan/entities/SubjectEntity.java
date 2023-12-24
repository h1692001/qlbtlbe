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
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String subjectType;
    private String subjectId;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "subject")
    private List<BTL> btls;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private ClassV classV;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "subject")
    private List<SubjectUserEntity> members;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "subject")
    private List<GroupEntity> groups;
}
