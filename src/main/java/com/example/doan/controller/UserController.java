package com.example.doan.controller;

import com.example.doan.dtos.FacultyDTO;
import com.example.doan.dtos.GetUserResponse;
import com.example.doan.dtos.MajorDto;
import com.example.doan.dtos.RegisterCommand;
import com.example.doan.entities.Faculties;
import com.example.doan.entities.MajorEntity;
import com.example.doan.entities.UserEntity;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.FacultyRepository;
import com.example.doan.repository.MajorRepository;
import com.example.doan.repository.UserRepository;
import com.example.doan.security.Role;
import com.example.doan.security.SecurityContants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FacultyRepository facultyRepository;

    private Key getSigningKey() {
        byte[] keyBytes = SecurityContants.getTokenSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @PostMapping("/registerStudent")
    @CrossOrigin
    private ResponseEntity<?> register(@RequestBody RegisterCommand registerCommand){
        UserEntity user=userRepository.findByUserIdOrEmail(registerCommand.getStudentId(),registerCommand.getEmail());
        if(user!=null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Existed user");
        }
        UserEntity userEntity=UserEntity.builder()
                .userId(registerCommand.getStudentId())
                .email(registerCommand.getEmail())
                .fullname(registerCommand.getFullname())
                .password(bCryptPasswordEncoder.encode("12345678"))
                .role(Role.STUDENT)
                .build();

        Optional<MajorEntity> majorEntity=majorRepository.findById(registerCommand.getMajorId());
        if(majorEntity.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Not found major!");
        }
        userEntity.setMajor(majorEntity.get());
        userEntity.setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");
        userRepository.save(userEntity);
        return ResponseEntity.ok("Created user success");
    }

    @PostMapping("/registerAdmin")
    @CrossOrigin
    private ResponseEntity<?> registerAdmin(@RequestBody RegisterCommand registerCommand){
        UserEntity user=userRepository.findByUserIdOrEmail(registerCommand.getStudentId(),registerCommand.getEmail());
        if(user!=null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Existed user");
        }
        UserEntity userEntity=UserEntity.builder()
                .userId(registerCommand.getStudentId())
                .email(registerCommand.getEmail())
                .fullname(registerCommand.getFullname())
                .password(bCryptPasswordEncoder.encode(registerCommand.getPassword()))
                .role(Role.ADMIN)
                .build();
        userEntity.setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");

        userRepository.save(userEntity);
        return ResponseEntity.ok("Created user success");
    }

    @PostMapping("/registerTeacher")
    @CrossOrigin
    private ResponseEntity<?> registerTeacher(@RequestBody RegisterCommand registerCommand){
        UserEntity user=userRepository.findByUserIdOrEmail(registerCommand.getStudentId(),registerCommand.getEmail());
        if(user!=null){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Existed user");
        }
        UserEntity userEntity=UserEntity.builder()
                .userId(registerCommand.getStudentId())
                .email(registerCommand.getEmail())
                .fullname(registerCommand.getFullname())
                .password(bCryptPasswordEncoder.encode("12345678"))
                .role(Role.TEACHER)
                .build();

        Optional<Faculties> majorEntity=facultyRepository.findById(registerCommand.getFacultyId());
        if(majorEntity.isEmpty()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Not found major!");
        }
        userEntity.setFaculty(majorEntity.get());
        userEntity.setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");

        userRepository.save(userEntity);
        return ResponseEntity.ok("Created user success");
    }

    @GetMapping("/getCurrentUser")
    @CrossOrigin
    private ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token.replace(SecurityContants.TOKEN_PREFIX, "")).getBody();
        String user = claims.getSubject();
        UserEntity userEntity=userRepository.findByEmail(user);
        return ResponseEntity.ok(GetUserResponse.builder()
                        .userId(userEntity.getUserId())
                        .id(userEntity.getId())
                        .avatar(userEntity.getAvatar())
                        .email(userEntity.getEmail())
                        .fullname(userEntity.getFullname())
                        .role(userEntity.getRole())
                .build());
    }

    @GetMapping("/getAllUsers")
    @CrossOrigin
    private ResponseEntity<?> getAllUsers(){
        List<UserEntity> users=userRepository.findAll();
        List<GetUserResponse> getUserResponses=new ArrayList<>();
        users.forEach(userEntity -> {
            GetUserResponse getUserResponse= GetUserResponse.builder()
                    .role(userEntity.getRole())
                    .userId(userEntity.getUserId())
                    .fullname(userEntity.getFullname())
                    .email(userEntity.getEmail())
                    .build();

            getUserResponses.add(getUserResponse);
        });
        return ResponseEntity.ok(getUserResponses);
    }

    @GetMapping("/getAllStudents")
    @CrossOrigin
    private ResponseEntity<?> getAllStudents(){
        List<UserEntity> users=userRepository.findByRole(Role.STUDENT);
        List<GetUserResponse> getUserResponses=new ArrayList<>();
        users.forEach(userEntity -> {
            GetUserResponse getUserResponse= GetUserResponse.builder()
                    .id(userEntity.getId())
                    .role(userEntity.getRole())
                    .userId(userEntity.getUserId())
                    .fullname(userEntity.getFullname())
                    .email(userEntity.getEmail())
                    .avatar(userEntity.getAvatar())
                    .major(MajorDto.builder()
                            .id(userEntity.getMajor().getId())
                            .majorName(userEntity.getMajor().getName()).build())
                    .faculty(FacultyDTO.builder()
                            .id(userEntity.getMajor().getFaculties().getId())
                            .name(userEntity.getMajor().getFaculties().getName())
                            .build())
                    .build();

            getUserResponses.add(getUserResponse);
        });
        return ResponseEntity.ok(getUserResponses);
    }
    @GetMapping("/getAllTeachers")
    @CrossOrigin
    private ResponseEntity<?> getAllTeachers(){
        List<UserEntity> users=userRepository.findByRole(Role.TEACHER);
        List<GetUserResponse> getUserResponses=new ArrayList<>();
        users.forEach(userEntity -> {
            GetUserResponse getUserResponse= GetUserResponse.builder()
                    .id(userEntity.getId())
                    .role(userEntity.getRole())
                    .userId(userEntity.getUserId())
                    .fullname(userEntity.getFullname())
                    .email(userEntity.getEmail())
                    .faculty(FacultyDTO.builder()
                            .id(userEntity.getFaculty().getId())
                            .name(userEntity.getFaculty().getName())
                            .build())
                    .build();

            getUserResponses.add(getUserResponse);
        });
        return ResponseEntity.ok(getUserResponses);
    }
}
