package com.se2020.course.registration.controller;
import java.util.*;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.enums.PermissionsEnum;
import com.se2020.course.registration.enums.RolesEnum;
import com.se2020.course.registration.repository.CourseRepository;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;
import com.se2020.course.registration.utils.PermissionUtils;
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
        if (PermissionUtils.hasPermission(PermissionsEnum.GET_USER, email,password, userRepository)){
            return userRepository.findAll();
        }
        return new ArrayList<>();
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public User getUser(@RequestParam("email") String email, @RequestParam("password") String password,
                              @PathVariable("userId") String userId){

        if (PermissionUtils.hasPermission(PermissionsEnum.GET_USER, email,password,userRepository)){
            return userRepository.findByUserId(userId);
        }
        return null;
    }


    /**
     * Adds a new user
     * @param user
     * @return
     */
    @PostMapping("/user")
    @ResponseBody
    public String addUser(@RequestParam("email") String email, @RequestParam("password") String password,
                          @RequestBody User user){
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADD_USER, email, password,userRepository)){
            return "Only Admin are allowed to add new user";
        }

        if (user.getEmail() == null | user.getUserId() == null){
            return "Not enough info to execute this action. Both email and user ID required.";
        }

        if (userRepository.existsByUserId(user.getUserId())){
            return "Existing user!";
        }

        if (user.getRole() == null){
            user.setRole("GUEST");
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

