package com.example.doan.controller;

import com.example.doan.dtos.AuthDto;
import com.example.doan.dtos.ClassDTO;
import com.example.doan.dtos.MajorDto;
import com.example.doan.dtos.SubjectDTO;
import com.example.doan.entities.ClassV;
import com.example.doan.entities.SubjectEntity;
import com.example.doan.entities.SubjectUserEntity;
import com.example.doan.entities.UserEntity;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.ClassVRepository;
import com.example.doan.repository.SubjectRepository;
import com.example.doan.repository.SubjectUserRepository;
import com.example.doan.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("subject")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ClassVRepository classVRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectUserRepository subjectUserRepository;
    @GetMapping("/getAllByClassV")
    public ResponseEntity<?> getAllByClassV(@RequestParam Long classId){
        ClassV classV=classVRepository.findById(classId).orElse(null);
        List<SubjectEntity> subjectEntityList=subjectRepository.findAllByClassV(classV);
        return ResponseEntity.ok(subjectEntityList.stream().map(sb->{
            SubjectUserEntity subjectUserEntity=subjectUserRepository.findTeacherInSubject(sb.getId());
            return SubjectDTO.builder()
                    .id(sb.getId())
                    .name(sb.getName())
                    .classV(ClassDTO.builder()
                            .id(sb.getClassV().getId())
                            .name(sb.getClassV().getClassName())
                            .major(MajorDto.builder()
                                    .id(sb.getClassV().getMajor().getId())
                                    .majorName(sb.getClassV().getMajor().getName())
                                    .build())
                            .build())
                    .teacher(ObjectUtils.isNotEmpty(subjectUserEntity)?subjectUserEntity.getUser().getFullname():"")
                    .build();
        }).collect(Collectors.toList()));
    }

    @GetMapping("/getAllByUser")
    public ResponseEntity<?> getAllByUser(@RequestParam Long userId){
       UserEntity user=userRepository.findById(userId).orElse(null);
        List<SubjectUserEntity> subjectUserEntities=subjectUserRepository.findAllByUser(user);
        return ResponseEntity.ok(subjectUserEntities.stream().map(sb->{
            return SubjectDTO.builder()
                    .id(sb.getId())
                    .name(sb.getSubject().getName())
                    .build();
        }).collect(Collectors.toList()));
    }
    @PostMapping
    public  ResponseEntity<?> addSubject(@RequestBody SubjectDTO subjectDTO){
        SubjectEntity check=subjectRepository.findByName(subjectDTO.getName()).orElse(null);
        if(check!=null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Existed subject");
        }
        ClassV classV=classVRepository.findById(subjectDTO.getClassId()).orElse(null);
         SubjectEntity subject=SubjectEntity.builder()
                 .name(subjectDTO.getName())
                 .classV(classV)
                 .build();
        SubjectEntity savedSubject= subjectRepository.save(subject);
        classV.getSubjects().add(subject);
        classVRepository.save(classV);
        classV.getClassVUsers().forEach(classVUser -> {
            if(classVUser.getRole().equals("STUDENT")){
                SubjectUserEntity subjectUserEntity=SubjectUserEntity.builder()
                        .user(classVUser.getUser())
                        .subject(savedSubject)
                        .role("STUDENT")
                        .build();
                subjectUserRepository.save(subjectUserEntity);
                classVUser.getUser().getSubjects().add(subjectUserEntity);
                userRepository.save(classVUser.getUser());
            }

        });
        return ResponseEntity.ok("ok");
    }

    @PutMapping
    public ResponseEntity<?> addTeacher(@RequestBody SubjectDTO subjectDTO){
        UserEntity user=userRepository.findById(subjectDTO.getUserId()).orElse(null);
        SubjectEntity subject=subjectRepository.findById(subjectDTO.getId()).orElse(null);
        SubjectUserEntity subjectUserEntity=SubjectUserEntity.builder()
                .user(user)
                .subject(subject)
                .role("TEACHER")
                .build();
        subjectUserRepository.save(subjectUserEntity);
        user.getSubjects().add(subjectUserEntity);
        subject.getMembers().add(subjectUserEntity);
        userRepository.save(user);
        subjectRepository.save(subject);
        return ResponseEntity.ok("");

    }
}
