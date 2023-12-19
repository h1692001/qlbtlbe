package com.example.doan.repository;

import com.example.doan.entities.SubjectEntity;
import com.example.doan.entities.SubjectUserEntity;
import com.example.doan.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectUserRepository extends JpaRepository<SubjectUserEntity,Long> {
    SubjectUserEntity findByUserAndSubject(UserEntity userEntity, SubjectEntity subjectEntity);

    List<SubjectUserEntity> findAllBySubject(SubjectEntity subjectEntity);

    @Query("select f from SubjectUserEntity f where f.subject.id=:subjectId and f.role=:role")
    List<SubjectUserEntity> findAllBySubjectStudent(@Param("subjectId") Long subjectId,String role);

    @Query("SELECT e from SubjectUserEntity e WHERE e.role='TEACHER' AND e.subject.id=:subjectId")
    SubjectUserEntity findTeacherInSubject(@Param("subjectId") Long subjectId);

    List<SubjectUserEntity> findAllByUser(UserEntity user);
}
