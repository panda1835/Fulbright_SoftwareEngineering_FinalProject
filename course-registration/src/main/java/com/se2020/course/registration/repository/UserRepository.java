package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByEmailAndPassword(@Param("email") String email, @Param("pass") String password);

    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByUserId(@Param("userId") String userId);

    boolean existsByUserId(@Param("userid") String userId);

    void deleteByUserId(@Param("userId") String userId);
}
