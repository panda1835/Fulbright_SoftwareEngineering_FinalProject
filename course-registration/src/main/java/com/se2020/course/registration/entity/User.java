package com.se2020.course.registration.entity;

import com.se2020.course.registration.utils.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String userName;
    private String userId;
    private String email;
    private String password;
    private String role;


    public User(String email, String password, String userId){
        this.email = email;
        this.password = SecurityUtils.hashPassword(password);
        this.userId = userId;
    }

}
