package com.example.doan.controller;

import com.example.doan.dtos.ClassDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    private ClassVUserRepository classVUserRepository;

    @GetMapping
    private ResponseEntity<?> getAllClass() {
        List<ClassV> classVList = classVRepository.findAll();
        return ResponseEntity.ok(classVList.stream().map(classV -> {
            return ClassDTO.builder()
                    .id(classV.getId())
                    .createdAt(classV.getCreatedAt())
                    .name(classV.getClassName())
                    .major(MajorDto.builder()
                            .id(classV.getMajor().getId())
                            .majorName(classV.getMajor().getName()).build())
                    .build();
        }).collect(Collectors.toList()));
    }
    @GetMapping("/getClassByUser")
    private ResponseEntity<?> getClassByUser(@RequestParam Long userId) {
        Optional<UserEntity> user=userRepository.findById(userId);

        List<ClassVUser> classVList = classVUserRepository.findAllByUser(user.get());
        return ResponseEntity.ok(classVList.stream().map(classV -> {
            return ClassDTO.builder()
                    .id(classV.getId())
                    .createdAt(classV.getClassV().getCreatedAt())
                    .name(classV.getClassV().getClassName())

                    .build();
        }).collect(Collectors.toList()));
    }
    @GetMapping("/getMembers")
    private ResponseEntity<?> getMembers(@RequestParam Long classId) {
        Optional<ClassV> classVList = classVRepository.findById(classId);
        List<ClassVUser> classVUsers=classVUserRepository.findAllByClassV(classVList.get());
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

    @PostMapping
    private ResponseEntity<?> createClass(@RequestBody ClassDTO classDTO) {
        ClassV classV=classVRepository.findByClassName(classDTO.getName());
        if(classV!=null){
            throw new ApiException("Existed class");
        }
        Optional<MajorEntity> majorEntity=majorRepository.findById(classDTO.getMajorId());
        ClassV classV1=ClassV.builder()
                .className(classDTO.getName())
                .createdAt(new Date())
                .major(majorEntity.get())
                .build();
        classVRepository.save(classV1);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/addMember")
    private ResponseEntity<?> addMember (@RequestBody ClassDTO classDTO) {
        Optional<ClassV> classV=classVRepository.findById(classDTO.getId());
        if(classV.isEmpty()){
            throw new ApiException("Can't find class");
        }

        Optional<UserEntity> user=userRepository.findById(classDTO.getMemberId());
        ClassVUser check=classVUserRepository.findByUserAndClassV(user.get(),classV.get());
        if(check!=null){
            throw new ApiException("Existed member");
        }

        ClassVUser classVUser= ClassVUser.builder()
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

}
