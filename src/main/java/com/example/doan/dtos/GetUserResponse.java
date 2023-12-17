package com.example.doan.dtos;

import com.example.doan.security.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponse {
    private Long id;
    private String userId;
    private String fullname;
    private String avatar;
    private String email;
    private Role role;
    private FacultyDTO faculty;
    private MajorDto major;
    private int status;
    private ClassDTO classDTO;
}
