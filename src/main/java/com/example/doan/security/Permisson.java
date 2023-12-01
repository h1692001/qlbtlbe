package com.example.doan.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permisson {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    STUDENT_READ("student:read"),
    STUDENT_CREATE("student:create"),
    STUDENT_DELETE("student:delete"),
    STUDENT_UPDATE("student:update"),
    TEACHER_READ("teacher:read"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_CREATE("teacher:create"),
    TEACHER_DELETE("teacher:delete");

    private final String permission;

}
