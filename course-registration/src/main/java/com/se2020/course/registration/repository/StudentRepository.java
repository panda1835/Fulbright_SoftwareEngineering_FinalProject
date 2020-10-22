package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;
public interface StudentRepository extends JpaRepository<Student, Long>{
    Optional<Student> findByStudentId(@Param("studentId") String studentId);
    void deleteByStudentId(@Param("studentId") String studentId);
    
}
