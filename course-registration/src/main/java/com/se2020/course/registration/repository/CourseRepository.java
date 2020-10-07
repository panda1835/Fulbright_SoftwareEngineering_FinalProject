package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(@Param("courseId") String courseId);
    void deleteByCourseId (@Param("courseId") String courseId);

}
