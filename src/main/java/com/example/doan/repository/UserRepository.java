package com.example.doan.repository;

import com.example.doan.entities.UserEntity;
import com.example.doan.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);

    Optional<UserEntity> findById(Long userId);

    UserEntity findByUserIdOrEmail(String studentId, String email);

    List<UserEntity> findByRole(Role role);

    List<UserEntity> findByIdIn(List<Long> uploader);

    List<UserEntity> findAllByRole(Role role);
}
