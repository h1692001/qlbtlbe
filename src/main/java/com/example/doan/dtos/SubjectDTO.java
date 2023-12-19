package com.example.doan.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class SubjectDTO {
    private Long id;

    private String name;
    private List<BTLDTO> btls;
    private String teacher;
    private GetUserResponse member;
    private Long userId;
    private int isSubmit;
    private ClassDTO classV;
    private Long classId;
    private Date submittedAt;
    private int status;
    private String role;



}
