package com.se2020.course.registration.entity;

import com.se2020.course.registration.utils.SecurityUtils;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String userName;
    private String userId;
    private String email;
    private String password;
    private String role;

    User(){}

    public User(String email, String password){
        this.email = email;
        this.password = SecurityUtils.hashPassword(password);
    }

}
