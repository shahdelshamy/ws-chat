package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJPARepository extends JpaRepository<User,Long> {
}
