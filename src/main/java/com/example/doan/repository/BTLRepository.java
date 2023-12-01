package com.example.doan.repository;

import com.example.doan.entities.BTL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BTLRepository extends JpaRepository<BTL,Long> {
}
