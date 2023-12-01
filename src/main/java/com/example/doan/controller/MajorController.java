package com.example.doan.controller;

import com.example.doan.dtos.MajorDto;
import com.example.doan.entities.MajorEntity;
import com.example.doan.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("major")
@RequiredArgsConstructor
public class MajorController {
    private final MajorRepository majorRepository;

    @GetMapping
    private ResponseEntity<List<MajorDto>> getAllMajor() {
        List<MajorEntity> majorEntityList=majorRepository.findAll();
        List<MajorDto> majorDtos=new ArrayList<>();
        majorEntityList.forEach(majorEntity -> {
            MajorDto majorDto= MajorDto.builder()
                    .majorName(majorEntity.getName())
                    .id(majorEntity.getId())
                    .build();
            majorDtos.add(majorDto);
        });
        return ResponseEntity.ok(majorDtos);
    }
}
