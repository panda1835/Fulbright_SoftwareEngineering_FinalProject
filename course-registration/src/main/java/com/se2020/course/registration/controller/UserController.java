package com.se2020.course.registration.controller;
import java.util.*;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.enums.PermissionsEnum;
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
     * Gets all users
     * @param email email of requester
     * @param password password of requester
     * @return list of users if requester has permission, otherwise an empty list
     */
    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUsers(@RequestParam("email") String email, @RequestParam("password") String password){
        if (PermissionUtils.hasPermission(PermissionsEnum.GET_USER, email,password, userRepository)){
            return userRepository.findAll();
        }
        return new ArrayList<>();
    }

    /**
     * Gets one user
     * @param email email of requester
     * @param password password of requester
     * @param userId user Id of person requester is looking up
     * @return the user's info if requester has permission, otherwise an null object
     */
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
     * Add a new user
     * @param email email of requester
     * @param password password of requester
     * @param user  the new user
     * @return "Sucess" if requester has permission, the user has both unique email and user ID
     * otherwise error message
     */
    @PostMapping("/user")
    @ResponseBody
    public String addUser(@RequestParam("email") String email, @RequestParam("password") String password,
                          @RequestBody User user){
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADD_USER, email, password,userRepository)){
            return "Only Admin are allowed to add new user";
        }

        if (user.getEmail() == null | user.getUserId() == null){
            return "Not enough info to execute this action. Both user's email and user ID required.";
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return "Existing email";
        }
        if (userRepository.findByUserId(user.getUserId()).isPresent()){
            return "Existing user id";
        }

        user.setRole(user.getRole().toUpperCase());
        user.setUserId(user.getUserId());
        String hashedPassword = SecurityUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "Success";
    }

    /**
     * Updates a user info
     * @param email email of requester
     * @param password password of requester
     * @param updatedUser the updated user
     * @param userId user Id of person requester is modifying
     * @return "Success" if requester has permission and the user ID is found,
     *          error message otherwise
     */
    @PutMapping("/user/{userId}")
    @ResponseBody
    public String updateUser (@RequestParam("email") String email, @RequestParam("password") String password,
                              @RequestBody User updatedUser, @PathVariable("userId") String userId){
        if (!PermissionUtils.hasPermission(PermissionsEnum.MODIFY_USER, email, password,userRepository)){
            return "Only Admin are allowed to update user info";
        }

        if (!userRepository.findByUserId(userId).isPresent()){
            return "User not found";
        }
        User user = userRepository.findByUserId(userId).get();
        user.setUserName(updatedUser.getUserName());
        user.setRole(updatedUser.getRole());
        userRepository.save(user);
        return "Success update";

    }

    /**
     *
     * @param email email of requester
     * @param password password of requester
     * @param userId user Id of person requester is deleting
     * @return "Success" if requester has permission and the user ID is found,
     *         error message otherwise
     */
    @DeleteMapping("/user/{userId}")
    @ResponseBody
    public String deleteUser(@RequestParam("email") String email, @RequestParam("password") String password,
                             @PathVariable("userId") String userId){
        if (!PermissionUtils.hasPermission(PermissionsEnum.DELETE_USER, email, password, userRepository)){
            return "Only Admin are allowed to delete user";
        }

        if (!userRepository.findByUserId(userId).isPresent()){
            return "User not found";
        }
        userRepository.deleteByUserId(userId);
        return "Success";
    }















}

