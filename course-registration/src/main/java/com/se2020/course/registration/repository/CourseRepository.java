package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
