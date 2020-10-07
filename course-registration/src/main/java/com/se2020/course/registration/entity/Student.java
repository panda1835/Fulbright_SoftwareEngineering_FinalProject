package com.se2020.course.registration.entity;

import java.util.List;

import com.se2020.course.registration.entity.Course;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class Student{
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String studentId;
    private String email;

    private String dob;
    private int gradYear;
    private String aboutMe;

    private int numCredits;

//    private List<String> pastCourses; // courseId
//    private List<String> currentRegisteredCourse; // courseId
//
    Student(){}
//

}