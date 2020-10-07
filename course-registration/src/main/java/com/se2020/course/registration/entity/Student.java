package com.se2020.course.registration.entity;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String hashedPassword;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "student_pastCourse",
        joinColumns = {@JoinColumn(name = "student_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    @JsonIgnoreProperties("students")
    private Set<Course> pastCourses;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "student_course",
        joinColumns = {@JoinColumn(name = "student_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    @JsonIgnoreProperties("students")
    private Set<Course> currentRegisteredCourse; // courseId

    Student(){}

    Student(String name, String studentId){
        this.name = name;
        this.studentId = studentId;
    }

    public String addCurrentCourse(Course course){
        if (currentRegisteredCourse == null){
            currentRegisteredCourse = new HashSet<>();
        }

        this.getCurrentRegisteredCourse().add(course);
        return "Success";
    }

    public String removeCurrentCourse(Course course){
        if (!currentRegisteredCourse.contains(course)){
            return "This course is not currently in your list";
        }

        this.getCurrentRegisteredCourse().remove(course);
        return "Success";
    }
}