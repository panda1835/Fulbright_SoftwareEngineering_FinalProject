package com.se2020.course.registration.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class StudentInfo{
    @Id
    @GeneratedValue

    private List<String> courses;
    private String name;
    private String dob;
    private int classOf;
    private String email;
    private String studentId;
    private String aboutMe;

    public StudentInfo(){}

    StudentInfo(String studentId){
        this.studentId = studentId;
    }
}