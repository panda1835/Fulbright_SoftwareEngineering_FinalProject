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
    long id;

    private String userName;
    private String email;
    private String hashedPassword;
    private String role;
    private String userId;
    
    
}
