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
    private Long id;

    private String name;
    private String studentId;
    private String email;

//    private String dob;
//    private int gradYear;
//    private String aboutMe;
//
//    private int numCredits;
//    private List<String> pastCourses; // courseId
//    private List<String> currentRegisteredCourse; // courseId
//
//    Student(){}
//
//    Student(String studentId){
//        this.studentId = studentId;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getStudentId() {
//        return studentId;
//    }
//
//    public void setStudentId(String studentId) {
//        this.studentId = studentId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getDob() {
//        return dob;
//    }
//
//    public void setDob(String dob) {
//        this.dob = dob;
//    }
//
//    public int getGradYear() {
//        return gradYear;
//    }
//
//    public void setGradYear(int gradYear) {
//        this.gradYear = gradYear;
//    }
//
//    public String getAboutMe() {
//        return aboutMe;
//    }
//
//    public void setAboutMe(String aboutMe) {
//        this.aboutMe = aboutMe;
//    }
//
//    public int getNumCredits() {
//        return numCredits;
//    }
//
//    public void setNumCredits(int numCredits) {
//        this.numCredits = numCredits;
//    }
//
//    public List<String> getPastCourses() {
//        return pastCourses;
//    }
//
//    public void setPastCourses(List<String> pastCourses) {
//        this.pastCourses = pastCourses;
//    }
//
//    public List<String> getCurrentRegisteredCourse() {
//        return currentRegisteredCourse;
//    }
//
//    public void setCurrentRegisteredCourse(List<String> currentRegisteredCourse) {
//        this.currentRegisteredCourse = currentRegisteredCourse;
//    }
}