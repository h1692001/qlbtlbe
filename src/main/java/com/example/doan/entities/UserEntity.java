package com.example.doan.entities;

import com.example.doan.security.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserEntity  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String userId;

    private String fullname;
    @Column(name="avatar")
    private String avatar="https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png";;
    private String email;
    private String password;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "user")
    private List<ClassVUser> classVUsers;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private MajorEntity major;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Faculties faculty;


    @OneToMany(mappedBy = "user")
    private List<SubjectUserEntity> subjects;

    private int status;

    @ManyToMany(mappedBy = "publisher")
    private Set<BTL> btls;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
