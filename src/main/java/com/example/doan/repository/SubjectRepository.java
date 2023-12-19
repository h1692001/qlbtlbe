package com.example.doan.repository;

import com.example.doan.entities.ClassV;
import com.example.doan.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity,Long> {
    List<SubjectEntity> findAllByClassV(ClassV classV);

    Optional<SubjectEntity> findByName(String name);
}
