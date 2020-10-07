package com.se2020.course.registration.entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.se2020.course.registration.entity.Course;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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

    @ElementCollection
    private Set<String> pastCourses;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "student_course",
        joinColumns = {@JoinColumn(name = "student_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    @JsonIgnoreProperties("students")
    private Set<Course> currentRegisteredCourse; // courseId

    public Student(){}

    Student(String studentId){
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