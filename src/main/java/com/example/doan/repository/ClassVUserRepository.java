package com.example.doan.repository;

import com.example.doan.entities.ClassV;
import com.example.doan.entities.ClassVUser;
import com.example.doan.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassVUserRepository extends JpaRepository<ClassVUser,Long> {
    List<ClassVUser> findAllByClassV(ClassV classV);

    ClassVUser findByUser(UserEntity userEntity);

    ClassVUser findByUserAndClassV(UserEntity userEntity, ClassV classV);

    List<ClassVUser> findAllByUser(UserEntity userEntity);

    List<ClassVUser> findAllByClassVAndRole(ClassV classV,String role);
}
