package com.se2020.course.registration.controller;
import java.util.*;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.Student;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.enums.PermissionsEnum;
import com.se2020.course.registration.repository.CourseRepository;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;
import com.se2020.course.registration.utils.PermissionUtils;
import com.se2020.course.registration.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    //ALL USERS
    /**
     * All user browse course
     */
    @GetMapping("/browse")
    public List<Course> browseCourse(){
        return courseRepository.findAll();
    }

    /**
     * Admin - USER APIs
     */

    /**
     * Gets all users
     * @param email email of requester
     * @param password password of requester
     * @return list of users if requester has permission, otherwise an empty list
     */
    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUsers(@RequestParam("email") String email, @RequestParam("password") String password){
        if (PermissionUtils.hasPermission(PermissionsEnum.GET_USER, email, password, userRepository)){
            return userRepository.findAll();
        }else{
            return new ArrayList<>();
        }
    }


    /**
     * Get one user
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
     * @return "Success" if requester has permission, the user has both unique email and user ID
     * otherwise error message
     */
    @PostMapping("/user")
    @ResponseBody
    public String addUser(@RequestParam("email") String email, @RequestParam("password") String password,
                          @RequestBody User user){
        if (userRepository.findAll().size() == 0){
            User firstUser = new User(email, password, "0"); //userId "0" stands for first user in repo
            firstUser.setRole("admin");
            userRepository.save(firstUser);
        }

        if (!PermissionUtils.hasPermission(PermissionsEnum.ADD_USER, email, password, userRepository)){
            return "Only Admin are allowed to add new user";
        }

        if (user.getEmail() == null | user.getUserId() == null){
            return "Not enough info to execute this action. Both user's email and user ID required.";
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()){ return "Existing email"; }
        if (userRepository.findByUserId(user.getUserId()).isPresent()){ return "Existing user id"; }

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

        if (userRepository.findByUserId(userId).isEmpty()){
            return "User not found";
        }
        User user = userRepository.findByUserId(userId).get();
        user.setUserName(updatedUser.getUserName());
        user.setRole(updatedUser.getRole());
        userRepository.save(user);
        return "Success";

    }

    /**
     * Remove a user from database
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

        if (userRepository.findByUserId(userId).isEmpty()){
            return "User not found";
        }
        userRepository.deleteByUserId(userId);
        return "Success";
    }

    /**
     * ADMIN -- COURSE API
     */

    /**
     * Retrieve list of courses
     * @param email
     * @param password
     */
    @GetMapping("/course")
    @ResponseBody
    public List<Course> getAllCourses(@RequestParam("email") String email, @RequestParam("password") String password){
        if (PermissionUtils.hasPermission(PermissionsEnum.GET_COURSE, email,password, userRepository)){
            return courseRepository.findAll();
        }
        return new ArrayList<>();
    }

    /**
     * Retrieve a course
     * @param email
     * @param password
     * @param courseId
     */
    @GetMapping("/course/{courseId}")
    @ResponseBody
    public Course getCourse(@RequestParam("email") String email, @RequestParam("password") String password,
                            @PathVariable("courseId") String courseId){
        if (PermissionUtils.hasPermission(PermissionsEnum.GET_COURSE, email,password,userRepository)){
            return courseRepository.findByCourseId(courseId).orElse(null);
        }
        return null;

    }

    /**
     * Put a new course to the database
     * @param email
     * @param password
     * @param course
     * @return
     */
    @PostMapping("/course")
    @ResponseBody
    public String addCourse(@RequestParam("email") String email, @RequestParam("password") String password,
                            @RequestBody Course course){
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADD_COURSE, email, password, userRepository)){
            return "Only Admin are allowed to add new course";
        }

        if (course.getCourseId() == null){
            return "Course ID required to execute this action.";
        }

        if (courseRepository.findByCourseId(course.getCourseId()).isPresent()){ return "Existing course id"; }

        courseRepository.save(course);
        return "Success";
    }

    /**
     * Update a course
     * @param email
     * @param password
     * @param updatedCourse
     * @param courseId
     * @return
     */
    @PutMapping("course/{courseId}")
    @ResponseBody
    public String updateCourse(@RequestParam("email") String email, @RequestParam("password") String password,
                               @RequestBody Course updatedCourse, @PathVariable("courseId") String courseId){
        if (!PermissionUtils.hasPermission(PermissionsEnum.MODIFY_COURSE, email, password,userRepository)){
            return "Only Admin are allowed to update course info";
        }

        if (courseRepository.findByCourseId(courseId).isEmpty()){
            return "Course not found";
        }

        Course course = courseRepository.findByCourseId(courseId).get();

        course.setCourseName(updatedCourse.getCourseName());

        // course.setProfessors(updatedCourse.getProfessors());
        course.setPrerequisite(updatedCourse.getPrerequisite());

        course.setSyllabus(updatedCourse.getSyllabus());
        course.setNumCredits(updatedCourse.getNumCredits());
        course.setCapacity(updatedCourse.getCapacity());

        course.setStartDate(updatedCourse.getStartDate());
        course.setEndDate(updatedCourse.getEndDate());
        course.setSchedule(updatedCourse.getSchedule());
        courseRepository.save(course);
        return "Success";
    }

    /**
     * Remove a course from database
     * @param email
     * @param password
     * @param courseId
     * @return
     */
    @DeleteMapping("course/{courseId}")
    @ResponseBody
    public String deleteCourse(@RequestParam("email") String email, @RequestParam("password") String password,
                               @PathVariable("courseId") String courseId){
        if (!PermissionUtils.hasPermission(PermissionsEnum.DELETE_COURSE, email, password, userRepository)){
            return "Only Admin are allowed to delete course";
        }

        if (courseRepository.findByCourseId(courseId).isEmpty()){
            return "Course not found";
        }
        courseRepository.deleteByCourseId(courseId);
        return "Success";
    }
    // STUDENT
    /**
     * Register for a course
     */
    @PutMapping("/student/register/{courseId}")
    public String courseRegister(@RequestBody String userId, @PathVariable String courseId){
        
        // check userId to be student
        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null){
            return "Invalid user Id";
        }
        String role = user.getRole();
        if (role.compareTo("student") != 0){
            return "Only student can access this page";
        }
 
        // create student object
        Student student = studentRepository.findByStudentId(userId).get();
        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        String studentId = student.getStudentId();
        
        // check capacity
        if (course.getStudentList().size() < course.getCapacity()){
            return "This course is already full";
        }

        // check student register status
        for (Student stu: course.getStudentList()){
            if (stu.getStudentId().compareTo(studentId) == 0){
                return "You already registered for this course";
            }
        }

        // check prerequisite
        Set<String> prerequisite = course.getPrerequisite();
        Set<Course> pastCourse = student.getPastCourses();
        Set<String> pastCourseId = new HashSet<>();
        for (Course c:pastCourse){
            pastCourseId.add(c.getCourseId());
        }
        for (String pre: prerequisite){
            if (pastCourseId.contains(pre)){
                continue;
            }
            return "You are not fulfill the prerequisite";
        }

        // success, update database
        Student updateStudent = studentRepository.getOne(student.getId());
        Course updateCourse = courseRepository.getOne(course.getId());
        updateStudent.addCurrentCourse(course);
        updateCourse.addStudent(student);
        studentRepository.save(updateStudent);
        courseRepository.save(updateCourse);

        return "You successfully register for this course";
    }

    /**
     * Cancel a course
     */
    @PutMapping("/course/cancel/{courseId}")
    public String courseCancel(@RequestBody String userId, @PathVariable String courseId){

        // check userId to be student
        User user = userRepository.findByUserId(userId).orElse(null);
        if (user == null){
            return "InvalId user Id";
        }
        String role = user.getRole();
        if (role.compareTo("student") != 0){
            return "Only student can access this page";
        }

        // create student object
        Student student = studentRepository.findByStudentId(userId).get();
        // check student in this course
        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        if (course == null){
            return "InvalId course Id";
        }
        if (!course.getStudentList().contains(student)){
            return "You are not in this course";
        }
        
        // check 2-week duration
        // ...

        // success, update database
        Student updateStudent = studentRepository.getOne(student.getId());
        Course updateCourse = courseRepository.getOne(course.getId());
        updateStudent.removeCurrentCourse(course);
        updateCourse.removeStudent(student);
        studentRepository.save(updateStudent);
        courseRepository.save(updateCourse);

        return "You successfully cancel this course";
    }

    /**
     * Edit profile
     */
    @PutMapping("/profile/edit/{role}/{studentId}")
    public String updateProfile(@RequestBody Student student, @PathVariable String role
                                                            , @PathVariable String studentId){
        // check student
        if (role.compareTo("student") == 0){
            Student updateStudent = studentRepository.getOne(student.getId());
            updateStudent.setAboutMe(student.getAboutMe());
            updateStudent.setDob(student.getDob());
            updateStudent.setHashedPassword(SecurityUtils.hashPassword(student.getHashedPassword()));
            studentRepository.save(updateStudent);
        }
        return "Success";
    }
}
