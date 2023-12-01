package com.example.doan.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.doan.security.Permisson.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(ADMIN_DELETE,
            ADMIN_UPDATE,
            ADMIN_READ,
            ADMIN_CREATE,
            STUDENT_DELETE, STUDENT_CREATE, STUDENT_UPDATE, STUDENT_READ,
            TEACHER_CREATE,TEACHER_DELETE,TEACHER_UPDATE,TEACHER_READ
    )),
    GUEST(Collections.emptySet()),
    STUDENT(Set.of(STUDENT_DELETE, STUDENT_CREATE, STUDENT_UPDATE, STUDENT_READ)),
    TEACHER(Set.of(TEACHER_CREATE,TEACHER_DELETE,TEACHER_UPDATE,TEACHER_READ));

    private final Set<Permisson> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
