package com.example.doan.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doan.dtos.ClassDTO;
import com.example.doan.dtos.FacultyDTO;
import com.example.doan.dtos.GetUserResponse;
import com.example.doan.dtos.MajorDto;
import com.example.doan.entities.ClassV;
import com.example.doan.entities.ClassVUser;
import com.example.doan.entities.MajorEntity;
import com.example.doan.entities.UserEntity;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.ClassVRepository;
import com.example.doan.repository.ClassVUserRepository;
import com.example.doan.repository.MajorRepository;
import com.example.doan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("classV")
public class ClassVController {
    @Autowired
    private ClassVRepository classVRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;


    @Autowired
    private ClassVUserRepository classVUserRepository;

    @GetMapping("/getAllAdmin")
    private ResponseEntity<?> getAllAdmin() {
        List<ClassV> classVList = classVRepository.findAll();
        return ResponseEntity.ok(classVList.stream().map(classV -> {
            return ClassDTO.builder()
                    .id(classV.getId())
                    .createdAt(classV.getCreatedAt())
                    .name(classV.getClassName())
                    .major(MajorDto.builder()
                            .id(classV.getMajor().getId())
                            .majorName(classV.getMajor().getName()).build())
                    .status(classV.getStatus())
                    .build();
        }).collect(Collectors.toList()));
    }
    @GetMapping
    private ResponseEntity<?> getAllClass() {
        List<ClassV> classVList = classVRepository.findAll();
        return ResponseEntity.ok(classVList.stream().map(classV -> {
            if (classV.getStatus() != 1)
                return ClassDTO.builder()
                        .id(classV.getId())
                        .createdAt(classV.getCreatedAt())
                        .name(classV.getClassName())
                        .major(MajorDto.builder()
                                .id(classV.getMajor().getId())
                                .majorName(classV.getMajor().getName()).build())
                        .build();
            return null;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/getClassByUser")
    private ResponseEntity<?> getClassByUser(@RequestParam Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);

        List<ClassVUser> classVList = classVUserRepository.findAllByUser(user.get());
        return ResponseEntity.ok(classVList.stream().map(classV -> {
            if (classV.getStatus() != 1)
                return ClassDTO.builder()
                        .id(classV.getClassV().getId())
                        .createdAt(classV.getClassV().getCreatedAt())
                        .name(classV.getClassV().getClassName())
                        .build();
            return null;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/getMembers")
    private ResponseEntity<?> getMembers(@RequestParam Long classId) {
        Optional<ClassV> classVList = classVRepository.findById(classId);
        List<ClassVUser> classVUsers = classVUserRepository.findAllByClassV(classVList.get());
        return ResponseEntity.ok(classVUsers.stream().map(classVUser -> {
            return ClassDTO.builder()
                    .member(GetUserResponse.builder()
                            .id(classVUser.getUser().getId())
                            .userId(classVUser.getUser().getUserId())
                            .fullname(classVUser.getUser().getFullname())
                            .email(classVUser.getUser().getEmail())
                            .build())
                    .role(classVUser.getRole())
                    .build();
        }).collect(Collectors.toList()));
    }

    @GetMapping("/getMembersStudent")
    private ResponseEntity<?> getMembersStudent(@RequestParam Long classId) {
        Optional<ClassV> classVList = classVRepository.findById(classId);
        List<ClassVUser> classVUsers = classVUserRepository.findAllByClassVAndRole(classVList.get(), "STUDENT");
        return ResponseEntity.ok(classVUsers.stream().map(classVUser -> {
            return ClassDTO.builder()
                    .member(GetUserResponse.builder()
                            .id(classVUser.getUser().getId())
                            .role(classVUser.getUser().getRole())
                            .userId(classVUser.getUser().getUserId())
                            .fullname(classVUser.getUser().getFullname())
                            .email(classVUser.getUser().getEmail())
                            .avatar(classVUser.getUser().getAvatar())
                            .major(MajorDto.builder()
                                    .id(classVUser.getUser().getMajor().getId())
                                    .majorName(classVUser.getUser().getMajor().getName()).build())
                            .faculty(FacultyDTO.builder()
                                    .id(classVUser.getUser().getMajor().getFaculties().getId())
                                    .name(classVUser.getUser().getMajor().getFaculties().getName())
                                    .build())
                            .status(classVUser.getUser().getStatus())
                            .build())
                    .role(classVUser.getRole())
                    .build();
        }).collect(Collectors.toList()));
    }

    @PostMapping
    private ResponseEntity<?> createClass(@RequestBody ClassDTO classDTO) throws Exception {


        ClassV classV = classVRepository.findByClassName(classDTO.getName());
        if (classV != null) {
            throw new ApiException("Existed class");
        }
        Map result = cloudinary.api().createFolder(classDTO.getName(), ObjectUtils.emptyMap());
        Optional<MajorEntity> majorEntity = majorRepository.findById(classDTO.getMajorId());
        ClassV classV1 = ClassV.builder()
                .className(classDTO.getName())
                .createdAt(new Date())
                .major(majorEntity.orElseThrow(() -> new ApiException("Major not found")))
                .build();
        classVRepository.save(classV1);

        return ResponseEntity.ok("Success");


    }

    @PutMapping
    private ResponseEntity<?> updateClass(@RequestBody ClassDTO classDTO) throws Exception {
        Optional<MajorEntity> majorEntity = majorRepository.findById(classDTO.getMajorId());
        ClassV classV1 = ClassV.builder()
                .id(classDTO.getId())
                .className(classDTO.getName())
                .createdAt(classDTO.getCreatedAt())
                .major(majorEntity.orElseThrow(() -> new ApiException("Major not found")))
                .build();
        classVRepository.save(classV1);

        return ResponseEntity.ok("Success");


    }

    @PostMapping("/addMember")
    private ResponseEntity<?> addMember(@RequestBody ClassDTO classDTO) {
        Optional<ClassV> classV = classVRepository.findById(classDTO.getId());
        if (classV.isEmpty()) {
            throw new ApiException("Can't find class");
        }
        if(classV.get().getStatus()==1){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Disable class");
        }

        Optional<UserEntity> user = userRepository.findById(classDTO.getMemberId());
        ClassVUser check = classVUserRepository.findByUserAndClassV(user.get(), classV.get());
        if (check != null) {
            throw new ApiException("Existed member");
        }

        ClassVUser classVUser = ClassVUser.builder()
                .role(classDTO.getRole())
                .user(user.get())
                .classV(classV.get())
                .build();
        user.get().getClassVUsers().add(classVUser);
        classV.get().getClassVUsers().add(classVUser);
        classVUserRepository.save(classVUser);
        userRepository.save(user.get());
        classVRepository.save(classV.get());
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/checknopbai")
    private ResponseEntity<?> checknopbai(@RequestParam Long classId) {
        Optional<ClassV> classV = classVRepository.findById(classId);
        List<ClassVUser> classVUsers = classVUserRepository.findAllByClassV(classV.get());
        return ResponseEntity.ok(classVUsers.stream().map(classVUser -> {
            return ClassDTO.builder()
                    .member(GetUserResponse.builder()
                            .id(classVUser.getUser().getId())
                            .userId(classVUser.getUser().getUserId())
                            .fullname(classVUser.getUser().getFullname())
                            .email(classVUser.getUser().getEmail())
                            .build())
                    .role(classVUser.getRole())
                    .isSubmit(classVUser.getSubmit())
                    .submittedAt(classVUser.getSubmitedAt())
                    .build();
        }).collect(Collectors.toList()));

    }

    @GetMapping("/disableClass")
    private ResponseEntity<?> disableClass(@RequestParam Long classId){
        Optional<ClassV> classV=classVRepository.findById(classId);
        classV.get().setStatus(1);
        classVRepository.save(classV.get());
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/enableClass")
    private ResponseEntity<?> enableClass(@RequestParam Long classId){
        Optional<ClassV> classV=classVRepository.findById(classId);
        classV.get().setStatus(0);
        classVRepository.save(classV.get());
        return ResponseEntity.ok("ok");
    }
}
