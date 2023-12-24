package com.example.doan.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ClassDTO {
    private Long id;
    private String name;
    private Date createdAt;
    private MajorDto major;
    private Long memberId;
    private Long majorId;
    private String role;
    private GetUserResponse member;
    private int isSubmit;
    private Date submittedAt;
    private FacultyDTO faculty;
    private int status;
    private List<SubjectDTO> subjects;
    private String classId;
    private String classType;
}
