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
    // private Set<String> schedule;

    // private List<String> professor; 

    @ElementCollection
    @CollectionTable(name = "prerequisite")
    private Set<String> prerequisite; // courseId

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
