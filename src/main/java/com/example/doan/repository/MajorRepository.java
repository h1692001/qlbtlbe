package com.example.doan.repository;

import com.example.doan.entities.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<MajorEntity,Long> {
    MajorEntity findByName(String majorName);
}
