package com.example.doan.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacultyDTO {
    private Long id;
    private String name;
}
