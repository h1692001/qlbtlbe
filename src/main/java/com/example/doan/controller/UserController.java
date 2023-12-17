package com.example.doan.controller;

import com.example.doan.dtos.*;
import com.example.doan.entities.*;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.FacultyRepository;
import com.example.doan.repository.MajorRepository;
import com.example.doan.repository.TokenForgotRepository;
import com.example.doan.repository.UserRepository;
import com.example.doan.security.Role;
import com.example.doan.security.SecurityContants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FacultyRepository facultyRepository;
    private final TokenForgotRepository tokenForgotRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    private Key getSigningKey() {
        byte[] keyBytes = SecurityContants.getTokenSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @PostMapping("/registerStudent")
    @CrossOrigin
    private ResponseEntity<?> register(@RequestBody RegisterCommand registerCommand) {
        UserEntity user = userRepository.findByUserIdOrEmail(registerCommand.getStudentId(), registerCommand.getEmail());
        if (user != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Existed user");
        }
        UserEntity userEntity = UserEntity.builder()
                .userId(registerCommand.getStudentId())
                .email(registerCommand.getEmail())
                .fullname(registerCommand.getFullname())
                .password(bCryptPasswordEncoder.encode("12345678"))
                .role(Role.STUDENT)
                .build();

        Optional<MajorEntity> majorEntity = majorRepository.findById(registerCommand.getMajorId());
        if (majorEntity.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Not found major!");
        }
        userEntity.setMajor(majorEntity.get());
        userEntity.setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");
        userRepository.save(userEntity);
        return ResponseEntity.ok("Created user success");
    }

    @PostMapping("/registerAdmin")
    @CrossOrigin
    private ResponseEntity<?> registerAdmin(@RequestBody RegisterCommand registerCommand) {
        UserEntity user = userRepository.findByUserIdOrEmail(registerCommand.getStudentId(), registerCommand.getEmail());
        if (user != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Existed user");
        }
        UserEntity userEntity = UserEntity.builder()
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
    private ResponseEntity<?> registerTeacher(@RequestBody RegisterCommand registerCommand) {
        UserEntity user = userRepository.findByUserIdOrEmail(registerCommand.getStudentId(), registerCommand.getEmail());
        if (user != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Existed user");
        }
        UserEntity userEntity = UserEntity.builder()
                .userId(registerCommand.getStudentId())
                .email(registerCommand.getEmail())
                .fullname(registerCommand.getFullname())
                .password(bCryptPasswordEncoder.encode("12345678"))
                .role(Role.TEACHER)
                .build();

        Optional<Faculties> majorEntity = facultyRepository.findById(registerCommand.getFacultyId());
        if (majorEntity.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Not found major!");
        }
        userEntity.setFaculty(majorEntity.get());
        userEntity.setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");

        userRepository.save(userEntity);
        return ResponseEntity.ok("Created user success");
    }

    @PutMapping("/updateTeacher")
    @CrossOrigin
    private ResponseEntity<?> update(@RequestBody RegisterCommand registerCommand) {
        Optional<UserEntity> user = userRepository.findById(registerCommand.getId());

        user.get().setUserId(registerCommand.getStudentId());
        user.get().setEmail(registerCommand.getEmail());
        user.get().setFullname(registerCommand.getFullname());
        user.get().setUserId(registerCommand.getStudentId());
        user.get().setUserId(registerCommand.getStudentId());
        Optional<Faculties> majorEntity = facultyRepository.findById(registerCommand.getFacultyId());
        if (majorEntity.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Not found major!");
        }
        user.get().setFaculty(majorEntity.get());
        user.get().setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");

        userRepository.save(user.get());
        return ResponseEntity.ok("Updated user success");
    }
    @PutMapping("/updateStudent")
    @CrossOrigin
    private ResponseEntity<?> updateStudent(@RequestBody RegisterCommand registerCommand) {
        Optional<UserEntity> user = userRepository.findById(registerCommand.getId());

        user.get().setUserId(registerCommand.getStudentId());
        user.get().setEmail(registerCommand.getEmail());
        user.get().setFullname(registerCommand.getFullname());
        user.get().setUserId(registerCommand.getStudentId());
        Optional<MajorEntity> majorEntity = majorRepository.findById(registerCommand.getMajorId());
        if (majorEntity.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Not found major!");
        }
        user.get().setMajor(majorEntity.get());
        user.get().setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");
        userRepository.save(user.get());
        return ResponseEntity.ok("Updated user success");
    }

    @GetMapping("/getCurrentUser")
    @CrossOrigin
    private ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token.replace(SecurityContants.TOKEN_PREFIX, "")).getBody();
        String user = claims.getSubject();
        UserEntity userEntity = userRepository.findByEmail(user);
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
    private ResponseEntity<?> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<GetUserResponse> getUserResponses = new ArrayList<>();
        users.forEach(userEntity -> {
            GetUserResponse getUserResponse = GetUserResponse.builder()
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
    private ResponseEntity<?> getAllStudents() {
        List<UserEntity> users = userRepository.findByRole(Role.STUDENT);
        List<GetUserResponse> getUserResponses = new ArrayList<>();
        users.forEach(userEntity -> {
            GetUserResponse getUserResponse = GetUserResponse.builder()
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
                    .status(userEntity.getStatus())
                    .build();

            getUserResponses.add(getUserResponse);
        });
        return ResponseEntity.ok(getUserResponses);
    }

    @GetMapping("/getAllTeachers")
    @CrossOrigin
    private ResponseEntity<?> getAllTeachers() {
        List<UserEntity> users = userRepository.findByRole(Role.TEACHER);
        List<GetUserResponse> getUserResponses = new ArrayList<>();
        users.forEach(userEntity -> {
            GetUserResponse getUserResponse = GetUserResponse.builder()
                    .id(userEntity.getId())
                    .role(userEntity.getRole())
                    .userId(userEntity.getUserId())
                    .fullname(userEntity.getFullname())
                    .email(userEntity.getEmail())
                    .faculty(FacultyDTO.builder()
                            .id(userEntity.getFaculty().getId())
                            .name(userEntity.getFaculty().getName())
                            .build())
                    .status(userEntity.getStatus())
                    .build();

            getUserResponses.add(getUserResponse);
        });
        return ResponseEntity.ok(getUserResponses);
    }

    @PostMapping("/forgotPassword")
    private ResponseEntity<?> forgotPassword(@RequestBody GetUserResponse rq) {
        try {
            UserEntity user = userRepository.findByEmail(rq.getEmail());
            if (user == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Can't find Email!");
            }
            TokenForgot tokenForgot = new TokenForgot();
            String token = Jwts.builder().setSubject(user.getEmail()).setExpiration(new Date(System.currentTimeMillis() + SecurityContants.EXPIRATION_TIME)).signWith(getSigningKey()).compact();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(rq.getEmail());
            mimeMessageHelper.setSubject("Thư xác nhận lấy lại mật khẩu");
            mimeMessageHelper.setText("Xin chào, hãy truy cập đường dẫn sau để lấy lại mật khẩu: http://localhost:3000/reset/" + token);
            tokenForgot.setToken(token);
            Instant now = Instant.now();

            Duration fiveMinutes = Duration.ofMinutes(5);
            Instant futureTime = now.plus(fiveMinutes);

            tokenForgot.setExpired(futureTime);
            javaMailSender.send(mimeMessage);
            tokenForgotRepository.save(tokenForgot);
        } catch (Exception e) {

        }
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/checkForgotToken")
    private ResponseEntity<?> checkForgotToken(@RequestParam String token) {
        TokenForgot tokenForgot = tokenForgotRepository.findByToken(token);
        if (tokenForgot != null && tokenForgot.getExpired().isAfter(Instant.now())) {
            return ResponseEntity.ok("ok");
        } else if (tokenForgot != null && tokenForgot.getExpired().isBefore(Instant.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Expired token");
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

    }

    @PostMapping("/resetPassword")
    private ResponseEntity<?> resetPassword(@RequestBody AuthDto authDto) {

        TokenForgot tokenForgot = tokenForgotRepository.findByToken(authDto.getToken());
        if (tokenForgot != null && tokenForgot.getExpired().isAfter(Instant.now())) {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authDto.getToken()).getBody();
            String email = claims.getSubject();
            UserEntity user = userRepository.findByEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(authDto.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("ok");
        } else if (tokenForgot != null && tokenForgot.getExpired().isBefore(Instant.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Expired token");
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

    }

    @PostMapping("/changePassword")
    private ResponseEntity<?> changePassword(@RequestBody AuthDto authDto) {
        UserEntity user = userRepository.findByEmail(authDto.getEmail());
        if(!bCryptPasswordEncoder.matches(authDto.getCurrentPassword(),user.getPassword())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Current password's wrong!");
        }
        user.setPassword(bCryptPasswordEncoder.encode(authDto.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/disableUser")
    private ResponseEntity<?> disableClass(@RequestParam Long userId){
        Optional<UserEntity> user=userRepository.findById(userId);
        user.get().setStatus(1);
        userRepository.save(user.get());
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/enableUser")
    private ResponseEntity<?> enableClass(@RequestParam Long userId){
        Optional<UserEntity> user=userRepository.findById(userId);
        user.get().setStatus(0);
        userRepository.save(user.get());
        return ResponseEntity.ok("ok");
    }
}
