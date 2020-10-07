package com.se2020.course.registration.entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.se2020.course.registration.entity.Student;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import lombok.Data;

@Entity
@Data
public class Course{ 
    @Id 
    @GeneratedValue
    long id;

    private String courseName;
    private String courseId;
    private String syllabus;
    private Date startDay; // TBD date
    private Date endDay; // TBD date
    private int capacity;
    private int currentCapacity;
    private int numCredits;

    @ElementCollection
    private Set<String> prerequisite;
    // private List<Date> classTime; // TBD date
    // private List<String> professor; // prof name

    @ManyToMany(mappedBy = "currentRegisteredCourse", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("currentRegisteredCourse")
    private Set<Student> studentList; // studentId
    

     
    Course(){}

    Course(String courseName, String courseId, Set<String> prerequisite){
        this.courseId = courseId;
        this.courseName = courseName;
        this.prerequisite = prerequisite;
    }

    /**
     * Register a new student
     */
    public String addStudent(Student student){
        if (studentList == null){
            studentList = new HashSet<>();
        }
        // check student in the list
        if (studentList.contains(student)){
            return "This student already registered for this course";
        }
        // add student
        studentList.add(student);
        return "Success";
    }

    /**
     * Unregister a new student
     */
    public String removeStudent(Student student){
        if (studentList == null){
            studentList = new HashSet<>();
        }
        // check student in the list
        if (!studentList.contains(student)){
            return "This student is not in this course";
        }
        // add student
        studentList.remove(student);
        return "Success";
    }
}
