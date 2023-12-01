package com.example.doan.dtos;

import com.example.doan.entities.MajorEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MajorDto {
    private String majorName;
    private Long id;
}
