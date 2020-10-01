package com.se2020.course.registration.entity;

import java.util.List;
import java.util.Date;
import com.se2020.course.registration.entity.Student;

import javax.persistence.*;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Course{ 
    @Id 
    @GeneratedValue
    long id;

    private String courseName;
    // private String courseId;
    private List<String> prerequisite; // courseId
    private String syllabus;
    private Date startDay; // TBD date
    private Date endDay; // TBD date
    private int capacity;
    private int currentCapacity;
    private int numCredits;
    String courseId;

    private List<String> studentList; // studentId
    private List<Date> classTime; // TBD date
    private List<String> professor; // prof name

     
    Course(){}

    Course(String courseId){
        this.courseId = courseId;
    }
}
