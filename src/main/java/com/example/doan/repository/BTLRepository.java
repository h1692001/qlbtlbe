package com.example.doan.repository;

import com.example.doan.entities.BTL;
import com.example.doan.entities.ClassV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BTLRepository extends JpaRepository<BTL,Long> {
    List<BTL> findAllByClassV(ClassV classV);
}
