package com.example.wschat.repository.jpa;

import com.example.wschat.model.entity.User;
import com.example.wschat.model.enums.UserStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJPARepository extends JpaRepository<User,Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByStatus(UserStatuses userStatuses);
}
