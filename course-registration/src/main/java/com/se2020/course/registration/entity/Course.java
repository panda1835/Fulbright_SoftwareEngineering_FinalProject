package com.se2020.course.registration.entity;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.se2020.course.registration.entity.Student;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course{
    // FIELDS:
    @Id
    @GeneratedValue
    private Long id;
    private String courseName;
    private String courseId;
    private String syllabus;
    private int numCredits;
    private int capacity;
    private String startDate;
    private String endDate;


    @ManyToMany(mappedBy = "currentRegisteredCourse", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("currentRegisteredCourse")
    private Set<Student> studentList = new HashSet<>(); // studentId

    Course(){}

    Course(String courseName, String courseId){
        this.courseId = courseId;
        this.courseName = courseName;
    }




}
