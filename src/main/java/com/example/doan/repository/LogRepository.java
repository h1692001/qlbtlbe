package com.example.doan.repository;

import com.example.doan.entities.LogEntity;
import com.example.doan.entities.LogEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity,Long> {
    @Query("SELECT log FROM LogEntity log WHERE log.createdAt BETWEEN :startDate AND :endDate AND log.type = :type")
    List<LogEntity> findLogsByDateRangeAndType(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("type") LogEnum type);

}
