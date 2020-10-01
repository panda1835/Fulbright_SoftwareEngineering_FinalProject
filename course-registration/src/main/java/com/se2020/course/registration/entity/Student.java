package com.se2020.course.registration.entity;

import java.util.List;

import com.se2020.course.registration.entity.Course;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Student{
    @Id 
    @GeneratedValue
    long id;

    private int numCredits;
    private String name;
    private String dob;
    private int gradYear;
    private String email;
    private String studentId;
    private String aboutMe;
    private List<String> pastCourses; // courseId
    private List<String> currentRegisteredCourse; // courseId

    public Student(){}

    Student(String studentId){
        this.studentId = studentId;
    }
}