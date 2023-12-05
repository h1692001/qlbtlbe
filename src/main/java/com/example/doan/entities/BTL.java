package com.example.doan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BTL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String path;

    private String status;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "btl_publisher",
            joinColumns = @JoinColumn(name = "btl_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> publisher;

    private Date createdAt;

    @ManyToOne
    private ClassV classV;

}
