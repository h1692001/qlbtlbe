package com.example.doan.repository;

import com.example.doan.entities.Faculties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculties,Long> {
    Faculties findByName(String facultyName);
}
