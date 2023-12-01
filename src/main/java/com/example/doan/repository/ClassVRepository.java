package com.example.doan.repository;

import com.example.doan.entities.ClassV;
import com.example.doan.entities.ClassVUser;
import com.example.doan.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassVRepository extends JpaRepository<ClassV,Long> {
    ClassV findByClassName(String className);


}
