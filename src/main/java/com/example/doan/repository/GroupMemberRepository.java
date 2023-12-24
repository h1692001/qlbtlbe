package com.example.doan.repository;

import com.example.doan.entities.GroupMember;
import com.example.doan.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {
    List<GroupMember> findAllByUser(UserEntity userEntity);
}
