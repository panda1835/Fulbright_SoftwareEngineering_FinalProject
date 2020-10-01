package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByUserID(@Param("userId") String userId);
}
