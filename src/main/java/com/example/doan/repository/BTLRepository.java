package com.example.doan.repository;

import com.example.doan.entities.BTL;
import com.example.doan.entities.ClassV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BTLRepository extends JpaRepository<BTL,Long> {
    List<BTL> findAllByClassV(ClassV classV);

    @Query("SELECT b FROM BTL b WHERE " +
            "(:keyword IS NULL OR LOWER(b.name) LIKE %:keyword%) " +
            "AND (:classV IS NULL OR b.classV = :classV)")
    List<BTL> searchByNameAndClassV(@Param("keyword") String keyword, @Param("classV") ClassV classV);
}
