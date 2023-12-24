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
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private int isSubmitted;
    private Date submittedAt;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> members;

    @ManyToOne
    private SubjectEntity subject;
}
