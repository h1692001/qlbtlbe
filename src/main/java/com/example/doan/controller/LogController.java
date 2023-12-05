package com.example.doan.controller;

import com.example.doan.dtos.LogDto;
import com.example.doan.entities.LogEntity;
import com.example.doan.entities.LogEnum;
import com.example.doan.entities.UserEntity;
import com.example.doan.repository.LogRepository;
import com.example.doan.repository.UserRepository;
import com.example.doan.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("log")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    private ResponseEntity<?> getAllUploadLog(@RequestParam Date startDate, @RequestParam Date endDate, @RequestParam LogEnum type) {
        List<LogEntity> logEntityList = logRepository.findLogsByDateRangeAndType(startDate, endDate, type);
        List<LogDto> list = logEntityList.stream().map(logEntity -> {
            return LogDto.builder()
                    .id(logEntity.getId())
                    .build();
        }).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getUserLog")
    private ResponseEntity<?> getLogUser() {
        List<UserEntity> userEntities = userRepository.findAllByRole(Role.STUDENT);
        List<UserEntity> userEntities2 = userRepository.findAllByRole(Role.TEACHER);
        return ResponseEntity.ok(LogDto.builder()
                .teachers(userEntities2.size())
                .students(userEntities.size())
                .build());
    }
}
