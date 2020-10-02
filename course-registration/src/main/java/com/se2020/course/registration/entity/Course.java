package com.se2020.course.registration.entity;

import java.util.Calendar;
import java.util.List;

import com.se2020.course.registration.entity.Student;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Course{
    // FIELDS:
    @Id
    @GeneratedValue
    private Long id;

    private String courseName;
    private String courseNumber;

//    private List<String> professor; // prof name
//
//    private List<String> prerequisite; // courseNumber
//    private String syllabus;
//    private int numCredits;
//    private int capacity;
//
//    private Calendar startDate;
//    private Calendar endDate;
//    private List<Calendar> schedule;
//
//    private List<String> studentList; // studentId


    //CONSTRUCTOR:
    Course(){}

    Course(String courseNumber){
        this.courseNumber = courseNumber;
    }


}
