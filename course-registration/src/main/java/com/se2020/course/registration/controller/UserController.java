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
            return userRepository.findByUserId(userId).orElse(null);
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

        if (user.getEmail() == null){
            return "User's email required to execute this action";
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return "Existing email";
        }

        if (userRepository.findByUserId(user.getUserId()).isPresent()){
            return "Existing user id";
        }
        if (user.getRole() == null){
            user.setRole("GUEST");
        }
        user.setRole(user.getRole().toUpperCase());
        user.setUserId(user.getUserId());
        String hashedPassword = SecurityUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "Success";
    }

    @PutMapping("/user/{userId}")
    @ResponseBody
    public String updateUser (@RequestParam("email") String email, @RequestParam("password") String password,
                              @RequestBody User updatedUser, @PathVariable("userId") String userId){
        if (!PermissionUtils.hasPermission(PermissionsEnum.MODIFY_USER, email, password,userRepository)){
            return "Only Admin are allowed to update user info";
        }

        userRepository.findByUserId(userId)
                .map(user -> {
                    user.setUserName(updatedUser.getUserName());
                    user.setRole(updatedUser.getRole());
                    return userRepository.save(user);
                })
                .orElseGet(()-> {

                    return userRepository.findByEmail(updatedUser.getEmail())
                            .map(user -> {
                                user.setUserId(userId);
                                user.setUserName(updatedUser.getUserName());
                                user.setRole(updatedUser.getRole());
                                return userRepository.save(user);
                            }).orElseGet(() ->{
                                updatedUser.setUserId(userId);
                                return userRepository.save(updatedUser);
                            });
                });

        return "Success update";
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

