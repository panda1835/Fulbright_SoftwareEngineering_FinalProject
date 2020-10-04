package com.se2020.course.registration.controller;
import java.util.*;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.enums.RolesEnum;
import com.se2020.course.registration.repository.CourseRepository;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;
import com.se2020.course.registration.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;


    // ADMIN APIs:

    /**
     * Views list of current users
     * @return list of current users
     */
    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUsers(@RequestParam("email") String email, @RequestParam("password") String password){
        String hashed = SecurityUtils.hashPassword(password);
        List<User> requesters = userRepository.findByEmailAndPassword(email, hashed);

        if (requesters.size() > 0) {
            User requester = requesters.get(0);
            String role = requester.getRole().toUpperCase();
            if (RolesEnum.valueOf(role).equals(RolesEnum.ADMIN)) {
                return userRepository.findAll();
            }

        }
        return new ArrayList<>();
    }
    /**
     * Adds a new user
     * @param user
     * @return
     */
    @PostMapping("/user")
    public String addUser(@RequestBody User user){
        if (userRepository.existsByUserId(user.getUserId())){
            return "User exists!";
        }
        user.setRole(user.getRole().toUpperCase());
        String hashedPassword = SecurityUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "Success";
    }

    /**
     * Removes a user
     * @param userId
     * @return
     */
    @DeleteMapping("/user/{userId}")
    public String removeUser(@PathVariable String userId){
        if (userRepository.existsByUserId(userId)){
            userRepository.deleteByUserId(userId);
            return "User removed";
        }
        return "User not found";
    }















}

