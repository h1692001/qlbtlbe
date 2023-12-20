package com.example.doan.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doan.dtos.BTLDTO;
import com.example.doan.dtos.ClassDTO;
import com.example.doan.dtos.GetUserResponse;
import com.example.doan.dtos.SubjectDTO;
import com.example.doan.entities.*;
import com.example.doan.repository.*;
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
    private final LogRepository logRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectUserRepository subjectUserRepository;

    @PostMapping
    private ResponseEntity<?> uploadBTL(@RequestParam MultipartFile file, @ModelAttribute("uploader") List<Long> uploader, @ModelAttribute("classV") Long subject, @ModelAttribute("name") String name) throws IOException {
        Optional<SubjectEntity> classV1 = subjectRepository.findById(subject);
        List<UserEntity> user = userRepository.findByIdIn(uploader);
        String publicId = classV1.get().getName() + "/" + file.getOriginalFilename();

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", publicId, "resource_type", "raw"));
        user.forEach(userEntity -> {
            SubjectUserEntity classVUser = subjectUserRepository.findByUserAndSubject(userEntity, classV1.get());
            classVUser.setSubmit(1);
            classVUser.setSubmitedAt(new Date());
            subjectUserRepository.save(classVUser);
        });
        String fileUrl = (String) uploadResult.get("url");
        BTL btl = BTL.builder()
                .subject(classV1.get())
                .createdAt(new Date())
                .publisher(user)
                .name(name)
                .path(fileUrl)
                .status("PENDING")
                .build();

        btlRepository.save(btl);
        LogEntity logEntity = LogEntity.builder()
                .type(LogEnum.UPLOAD)
                .createdAt(new Date())
                .build();
        logRepository.save(logEntity);
        return ResponseEntity.ok("Uploaded file!");
    }

    @GetMapping
    private ResponseEntity<?> getAllByClass(@RequestParam Long classId) {
        ClassV classV=classVRepository.findById(classId).orElse(null);
        List<SubjectEntity> subjectEntityList = subjectRepository.findAllByClassV(classV);
        List<BTLDTO> btldtos = new ArrayList<>();
        subjectEntityList.forEach(subjectEntity -> {
            List<BTL> btlList = btlRepository.findAllBySubject(subjectEntity);
            btlList.forEach(btl -> {
                BTLDTO btldto = BTLDTO.builder()
                        .id(btl.getId())
                        .status(btl.getStatus())
                        .name(btl.getName())
                        .path(btl.getPath())
                        .createdAt(btl.getCreatedAt())
                        .subjectDTO(SubjectDTO.builder()
                                .id(btl.getSubject().getId())
                                .name(btl.getSubject().getName())
                                .build())
                        .publisher(btl.getPublisher().stream().map(p -> {
                            return GetUserResponse.builder()
                                    .id(p.getId())
                                    .userId(p.getUserId())
                                    .fullname(p.getFullname())
                                    .email(p.getEmail())
                                    .build();
                        }).collect(Collectors.toList()))
                        .subjectDTO(SubjectDTO.builder()
                                .id(btl.getSubject().getId())
                                .name(btl.getSubject().getName())
                                .build())
                        .build();
                btldtos.add(btldto);
            });
        });
        return ResponseEntity.ok(btldtos);
    }
    @GetMapping("/getAllBySubject")
    private ResponseEntity<?> getAllBySubject(@RequestParam Long classId) {
        SubjectEntity subjectEntity = subjectRepository.findById(classId).orElse(null);
        List<BTLDTO> btldtos = new ArrayList<>();
            List<BTL> btlList = btlRepository.findAllBySubject(subjectEntity);
            btlList.forEach(btl -> {
                BTLDTO btldto = BTLDTO.builder()
                        .id(btl.getId())
                        .status(btl.getStatus())
                        .name(btl.getName())
                        .path(btl.getPath())
                        .createdAt(btl.getCreatedAt())
                        .subjectDTO(SubjectDTO.builder()
                                .id(btl.getSubject().getId())
                                .name(btl.getSubject().getName())
                                .build())
                        .publisher(btl.getPublisher().stream().map(p -> {
                            return GetUserResponse.builder()
                                    .id(p.getId())
                                    .userId(p.getUserId())
                                    .fullname(p.getFullname())
                                    .email(p.getEmail())
                                    .build();
                        }).collect(Collectors.toList()))
                        .subjectDTO(SubjectDTO.builder()
                                .id(btl.getId())
                                .name(btl.getName())
                                .build())
                        .build();
                btldtos.add(btldto);
            });

        return ResponseEntity.ok(btldtos);
    }

    @PostMapping("/changeStatus")
    private ResponseEntity<?> approveBTL(@RequestBody BTLDTO btldto) {
        Optional<BTL> btl = btlRepository.findById(btldto.getId());
        btl.get().setStatus(btldto.getStatus());
        btlRepository.save(btl.get());
        LogEntity logEntity = LogEntity.builder()
                .type(btldto.getStatus().equals("APPROVE") ? LogEnum.APPROVE : LogEnum.CANCEL)
                .createdAt(new Date())
                .build();
        logRepository.save(logEntity);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/searchBTL")
    private ResponseEntity<List<BTLDTO>> searchBTL(@RequestParam String name, @RequestParam Long subjectId) {
        List<BTL> result;

        if (name != null && subjectId != -1) {
            result = btlRepository.searchByNameAndSubject(name, subjectRepository.findById(subjectId).orElse(null));
        } else if (name != null) {
            result = btlRepository.searchByNameAndSubject(name, null);
        } else if (subjectId != null) {
            result = btlRepository.searchByNameAndSubject(null, subjectRepository.findById(subjectId).orElse(null));
        } else {
            result = btlRepository.findAll();
        }

        List<BTLDTO> dtoList = result.stream()
                .map(dl->{
                   return BTLDTO.builder()
                           .id(dl.getId())
                           .name(dl.getName())
                           .createdAt(dl.getCreatedAt())
                           .status(dl.getStatus())
                           .publisher(dl.getPublisher().stream().map(d->{
                               return GetUserResponse.builder()
                                       .userId(d.getUserId())
                                       .fullname(d.getFullname())
                                       .email(d.getEmail())
                                       .id(d.getId())
                                       .build();
                           }).collect(Collectors.toList()))
                           .path(dl.getPath())
                           .subjectDTO(SubjectDTO.builder()
                                   .id(dl.getSubject().getId())
                                   .name(dl.getSubject().getName())
                                   .build())
                           .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
}
