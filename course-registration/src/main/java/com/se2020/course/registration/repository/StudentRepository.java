package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
    List<User> findByStudentID(@Param("studentId") String studentId);
    
}
