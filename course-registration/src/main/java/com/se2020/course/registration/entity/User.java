package com.se2020.course.registration.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue

    private String userName;
    private String email;
    private String hashedPassword;
    private String role;
    
    
}
