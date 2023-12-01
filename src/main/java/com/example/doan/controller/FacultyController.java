package com.example.doan.controller;

import com.example.doan.dtos.FacultyDTO;
import com.example.doan.dtos.MajorDto;
import com.example.doan.entities.Faculties;
import com.example.doan.entities.MajorEntity;
import com.example.doan.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("faculty")
@RequiredArgsConstructor
public class FacultyController {
    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping
    private ResponseEntity<List<FacultyDTO>> getAllMajor() {
        List<Faculties> majorEntityList=facultyRepository.findAll();
        List<FacultyDTO> majorDtos=new ArrayList<>();
        majorEntityList.forEach(majorEntity -> {
            FacultyDTO majorDto= FacultyDTO.builder()
                    .name(majorEntity.getName())
                    .id(majorEntity.getId())
                    .build();
            majorDtos.add(majorDto);
        });
        return ResponseEntity.ok(majorDtos);
    }
}
