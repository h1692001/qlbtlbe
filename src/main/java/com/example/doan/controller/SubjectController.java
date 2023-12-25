package com.example.doan.controller;

import com.example.doan.dtos.AuthDto;
import com.example.doan.dtos.ClassDTO;
import com.example.doan.dtos.MajorDto;
import com.example.doan.dtos.SubjectDTO;
import com.example.doan.entities.*;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @GetMapping("/getAllByClassV")
    public ResponseEntity<?> getAllByClassV(@RequestParam Long classId) {
        ClassV classV = classVRepository.findById(classId).orElse(null);
        List<SubjectEntity> subjectEntityList = subjectRepository.findAllByClassV(classV);
        return ResponseEntity.ok(subjectEntityList.stream().map(sb -> {
            SubjectUserEntity subjectUserEntity = subjectUserRepository.findTeacherInSubject(sb.getId());
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
                    .teacher(ObjectUtils.isNotEmpty(subjectUserEntity) ? subjectUserEntity.getUser().getFullname() : "")
                    .subjectId(sb.getSubjectId())
                    .subjectType(sb.getSubjectType())
                    .build();
        }).collect(Collectors.toList()));
    }

    @GetMapping("/getAllByUser")
    public ResponseEntity<?> getAllByUser(@RequestParam Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        List<SubjectUserEntity> subjectUserEntities = subjectUserRepository.findAllByUser(user);
        return ResponseEntity.ok(subjectUserEntities.stream().map(sb -> {
            return SubjectDTO.builder()
                    .id(sb.getSubject().getId())
                    .name(sb.getSubject().getName())
                    .subjectType(sb.getSubject().getSubjectType())
                    .role(sb.getSubject().getClassV().getClassName())
                    .build();
        }).collect(Collectors.toList()));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<SubjectEntity> subjectEntityList = subjectRepository.findAll();
        return ResponseEntity.ok(subjectEntityList.stream().map(sb -> {
            return SubjectDTO.builder()
                    .id(sb.getId())
                    .name(sb.getName())
                    .build();
        }).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> addSubject(@RequestBody SubjectDTO subjectDTO) {
        ClassV classV = classVRepository.findById(subjectDTO.getClassId()).orElse(null);
        SubjectEntity check = subjectRepository.findByNameAndClassV(subjectDTO.getName(), classV).orElse(null);
        if (check != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Existed subject");
        }
        SubjectEntity subject = SubjectEntity.builder()
                .name(subjectDTO.getName())
                .classV(classV)
                .subjectId(subjectDTO.getSubjectId())
                .subjectType(subjectDTO.getSubjectType())
                .build();
        SubjectEntity savedSubject = subjectRepository.save(subject);
        classV.getSubjects().add(subject);
        classVRepository.save(classV);
        classV.getClassVUsers().forEach(classVUser -> {
            if (classVUser.getRole().equals("STUDENT")) {
                SubjectUserEntity subjectUserEntity = SubjectUserEntity.builder()
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
    public ResponseEntity<?> addTeacher(@RequestBody SubjectDTO subjectDTO) {
        UserEntity user = userRepository.findById(subjectDTO.getUserId()).orElse(null);
        SubjectEntity subject = subjectRepository.findById(subjectDTO.getId()).orElse(null);
        SubjectUserEntity check = subjectUserRepository.findTeacherInSubject(subjectDTO.getId());
        if (check != null) {
            check.setUser(user);
            user.getSubjects().add(check);
            subjectUserRepository.save(check);
            userRepository.save(user);
        } else {
            SubjectUserEntity subjectUserEntity = SubjectUserEntity.builder()
                    .user(user)
                    .subject(subject)
                    .role("TEACHER")
                    .build();
            subjectUserRepository.save(subjectUserEntity);
            user.getSubjects().add(subjectUserEntity);
            subject.getMembers().add(subjectUserEntity);
            userRepository.save(user);
            subjectRepository.save(subject);
        }
        return ResponseEntity.ok("");
    }

    @PutMapping("/addGroup")
    private ResponseEntity<?> addGroup(@RequestBody SubjectDTO subjectDTO) {
        SubjectEntity subject = subjectRepository.findById(subjectDTO.getId()).orElse(null);
        GroupEntity groupEntity = groupRepository.findByNameAndSubject(subjectDTO.getName(), subject);
        if (groupEntity != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Existed group");
        }
        List<UserEntity> userEntities = userRepository.findAllById(subjectDTO.getUserIds());
        userEntities.forEach(userEntity -> {
            List<GroupMember> groupMember=groupMemberRepository.findAllByUser(userEntity);
            groupMember.forEach(groupMember1 -> {
                if(groupMember1.getGroup().getSubject().getId()==subject.getId()){
                    throw new ApiException(HttpStatus.BAD_REQUEST,"User");
                }
            });
        });
        List<GroupMember> groupMembers = userEntities.stream().map(userEntity -> {
            return GroupMember.builder()
                    .user(userEntity).build();
        }).collect(Collectors.toList());
        GroupEntity groupEntity1 = GroupEntity.builder()
                .isSubmitted(0)
                .subject(subject)
                .name(subjectDTO.getName())
                .members(groupMembers)
                .build();
        groupRepository.save(groupEntity1);
        groupMembers.forEach(groupMember -> {
            groupMember.setGroup(groupEntity1);
            userEntities.forEach(userEntity -> {
                userEntity.setGroupMembers(groupMembers);
            });
        });

        groupMemberRepository.saveAll(groupMembers);
        userRepository.saveAll(userEntities);
        return ResponseEntity.ok("ok");


    }
}
