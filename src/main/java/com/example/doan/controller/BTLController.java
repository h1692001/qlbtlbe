package com.example.doan.controller;

import com.example.doan.repository.BTLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("btl")
@RequiredArgsConstructor
public class BTLController {

    private final ResourceLoader resourceLoader;

    private final BTLRepository btlRepository;

    @PostMapping
    private ResponseEntity<?> uploadBTL(@RequestParam MultipartFile file,@ModelAttribute("uploader") Long uploader, @ModelAttribute("classV") Long classV){
        Resource fileOld = resourceLoader.getResource("classpath:static/btl");
    }
}
