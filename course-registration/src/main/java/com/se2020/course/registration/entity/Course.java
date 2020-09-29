package com.se2020.course.registration.entity;

import java.util.List;

import com.se2020.course.registration.entity.Student;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Course{ 
    @Id
    @GeneratedValue

    private String courseName;
    private String courseId;
    private List<String> prerequisite;
    private String syllabus;
    private String startDay;
    private String endDay;
    private List<Student> studentList;
    
    Course(){}

    Course(String courseId){
        this.courseId = courseId;
    }
}
