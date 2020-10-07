package com.se2020.course.registration.entity;

import java.util.Calendar;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "professors")
    private List<String> professors; // prof name

    @ElementCollection
    @CollectionTable(name = "prerequisites")
    private List<String> prerequisites; // courseId

    private String syllabus;
    private int numCredits;
    private int capacity;

    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @Temporal(TemporalType.DATE)
    private Calendar endDate;

    @ElementCollection
    @Temporal(TemporalType.TIMESTAMP)
    private List<Calendar> schedule;

//    private List<String> studentList; // studentId

    //CONSTRUCTORS:
    Course(){}

    Course(String courseId){
        this.courseId = courseId;
    }


}
