package com.example.doan.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doan.dtos.BTLDTO;
import com.example.doan.dtos.ClassDTO;
import com.example.doan.dtos.GetUserResponse;
import com.example.doan.entities.BTL;
import com.example.doan.entities.ClassV;
import com.example.doan.entities.ClassVUser;
import com.example.doan.entities.UserEntity;
import com.example.doan.repository.BTLRepository;
import com.example.doan.repository.ClassVRepository;
import com.example.doan.repository.ClassVUserRepository;
import com.example.doan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("btl")
@RequiredArgsConstructor
public class BTLController {

    private final Cloudinary cloudinary;

    private final BTLRepository btlRepository;
    private final UserRepository userRepository;
    private final ClassVRepository classVRepository;
    private final ClassVUserRepository classVUserRepository;

    @PostMapping
    private ResponseEntity<?> uploadBTL(@RequestParam MultipartFile file,@ModelAttribute("uploader") List<Long> uploader, @ModelAttribute("classV") Long classV, @ModelAttribute("name") String name) throws IOException {
        Optional<ClassV> classV1=classVRepository.findById(classV);
        List<UserEntity> user=userRepository.findByIdIn(uploader);
        String publicId = classV1.get().getClassName() + "/" + file.getOriginalFilename();

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", publicId,"resource_type", "raw"));
        user.forEach(userEntity -> {
            ClassVUser classVUser=classVUserRepository.findByUserAndClassV(userEntity,classV1.get());
            classVUser.setSubmit(1);
            classVUser.setSubmitedAt(new Date());
            classVUserRepository.save(classVUser);
        });
        String fileUrl = (String) uploadResult.get("url");
        BTL btl=BTL.builder()
                .classV(classV1.get())
                .createdAt(new Date())
                .publisher(user)
                .name(name)
                .path(fileUrl)
                .status("PENDING")
                .build();
        btlRepository.save(btl);
        return ResponseEntity.ok("Uploaded file!");
    }

    @GetMapping
    private ResponseEntity<?> getAllByClass(@RequestParam Long classId){
        Optional<ClassV> classV=classVRepository.findById(classId);
        List<BTL> btlList=btlRepository.findAllByClassV(classV.get());
        List<BTLDTO> btldtos=new ArrayList<>();
        btlList.forEach(btl -> {
            BTLDTO btldto=BTLDTO.builder()
                    .id(btl.getId())
                    .status(btl.getStatus())
                    .name(btl.getName())
                    .path(btl.getPath())
                    .createdAt(btl.getCreatedAt())
                    .classDTO(ClassDTO.builder()
                            .id(btl.getClassV().getId())
                            .name(btl.getClassV().getClassName())
                            .build())
                    .publisher(btl.getPublisher().stream().map(p->{
                        return GetUserResponse.builder()
                                .id(p.getId())
                                .userId(p.getUserId())
                                .fullname(p.getFullname())
                                .email(p.getEmail())
                                .build();
                    }).collect(Collectors.toList()))
                    .build();

            btldtos.add(btldto);
        });
            return ResponseEntity.ok(btldtos);
    }

    @PostMapping("/changeStatus")
    private ResponseEntity<?> approveBTL(@RequestBody BTLDTO btldto){
        Optional<BTL> btl=btlRepository.findById(btldto.getId());
        btl.get().setStatus(btldto.getStatus());
        btlRepository.save(btl.get());
        return ResponseEntity.ok("Success");
    }

}
