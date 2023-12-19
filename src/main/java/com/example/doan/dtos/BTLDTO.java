package com.example.doan.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BTLDTO {
    private Long id;

    private String path;
    private String name;
    private String status;
    private Date createdAt;
    private List<GetUserResponse> publisher;
    private SubjectDTO subjectDTO;
    private ClassDTO classDTO;
}
