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
    private List<String> prerequisite; // courseId
    private String syllabus;
    private String startDay; // TBD date
    private String endDay; // TBD date
    private List<String> studentList; // studentId
    private List<String> classTime; // TBD date
    private int numCredits;
    private List<String> professor; // prof name
    private int capacity;
     
    Course(){}

    Course(String courseId){
        this.courseId = courseId;
    }
}
