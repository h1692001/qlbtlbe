package com.example.doan.repository;

import com.example.doan.entities.GroupEntity;
import com.example.doan.entities.SubjectEntity;
import com.example.doan.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity,Long> {
    GroupEntity findByNameAndSubject(String name, SubjectEntity subject);

    List<GroupEntity> findALlBySubject(SubjectEntity subjectEntity);


    List<GroupEntity> findAllBySubject(SubjectEntity subjectEntity);
}
