package com.example.doan.dtos;

import com.example.doan.entities.Faculties;
import com.example.doan.entities.MajorEntity;

import com.example.doan.security.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCommand {
    private String studentId;
    private String fullname;
    private String email;
    private String password;
    private Long majorId;
    private Long facultyId;
}
