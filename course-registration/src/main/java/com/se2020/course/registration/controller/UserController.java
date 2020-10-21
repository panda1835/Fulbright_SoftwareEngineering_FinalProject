package com.se2020.course.registration.controller;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    // ALL USERS
    /**
     * All user browse course
     */
    @GetMapping("/browse")
    public List<Course> browseCourse() {
        return courseRepository.findAll();
    }

    /**
     * Login
     */
    @GetMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password){
        String hashed = SecurityUtils.hashPassword(password);
        List<User> user = userRepository.findByEmailAndPassword(email, hashed);
        if (user.isEmpty()){
            return "Wrong email or password";
        }
        return "Success";
    }

    /**
     * Change password
     */
    @PutMapping("/profile/password")
    public String changePassword(@RequestParam("email") String email, @RequestParam("password") String password,
                                 @RequestParam("new_password") String newPassword){
        if (!PermissionUtils.hasPermission(PermissionsEnum.CHANGE_PASSWORD, email, password, userRepository)){
            return "You don't have permission to perform this action";
        }
        String hashed = SecurityUtils.hashPassword(password);
        List<User> users = userRepository.findByEmailAndPassword(email, hashed);
        if (users.isEmpty()){
            return "Wrong email or password";
        }
        User user = users.get(0);
        if (newPassword.equals(password)){
            return "New password must be different from current password";
        }
        user.setPassword(SecurityUtils.hashPassword(newPassword));
        userRepository.save(user);
        return "Success";
    }

    /**
     * Admin - USER APIs
     */

    /**
     * Gets all users
     * 
     * @param email    email of requester
     * @param password password of requester
     * @return list of users if requester has permission, otherwise an empty list
     */
    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUsers(@RequestParam("email") String email, @RequestParam("password") String password) {
        if (PermissionUtils.hasPermission(PermissionsEnum.ADMIN_GET_USER, email, password, userRepository)) {
            return userRepository.findAll();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Get one user
     * 
     * @param email    email of requester
     * @param password password of requester
     * @param userId   user Id of person requester is looking up
     * @return the user's info if requester has permission, otherwise an null object
     */
    @GetMapping("/user/{userId}")
    @ResponseBody
    public User getUser(@RequestParam("email") String email, @RequestParam("password") String password,
            @PathVariable("userId") String userId) {

        if (PermissionUtils.hasPermission(PermissionsEnum.ADMIN_GET_USER, email, password, userRepository)) {
            return userRepository.findByUserId(userId).orElse(null);
        }
        return null;
    }

    /**
     * Add a new user
     * 
     * @param email    email of requester
     * @param password password of requester
     * @param user     the new user
     * @return "Success" if requester has permission, the user has both unique email
     *         and user ID otherwise error message
     */
    @PostMapping("/user")
    @ResponseBody
    public String addUser(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestBody User user) {
        if (userRepository.findAll().size() == 0) {
            User firstUser = new User(email, password, "0"); // userId "0" stands for first user in repo
            firstUser.setRole("admin");
            userRepository.save(firstUser);
        }

        if (!PermissionUtils.hasPermission(PermissionsEnum.ADMIN_ADD_USER, email, password, userRepository)) {
            return "Only Admin are allowed to add new user";
        }

        if (user.getEmail() == null | user.getUserId() == null) {
            return "Not enough info to execute this action. Both user's email and user ID required.";
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Existing email";
        }
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            return "Existing user id";
        }

        String hashedPassword = SecurityUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        if (user.getRole().toLowerCase().equals("student")){
            Student student = new Student(user.getUserName(), user.getUserId(), user.getEmail());
            studentRepository.save(student);
        }

        return "Success";
    }

    /**
     * Updates a user info
     * 
     * @param email       email of requester
     * @param password    password of requester
     * @param updatedUser the updated user
     * @param userId      user Id of person requester is modifying
     * @return "Success" if requester has permission and the user ID is found, error
     *         message otherwise
     */
    @PutMapping("/user/{userId}")
    @ResponseBody
    public String updateUser(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestBody User updatedUser, @PathVariable("userId") String userId) {
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADMIN_MODIFY_USER_ACCOUNT, email, password, userRepository)) {
            return "Only Admin are allowed to update user info";
        }

        if (userRepository.findByUserId(userId).isEmpty()) {
            return "User not found";
        }
        User user = userRepository.findByUserId(userId).get();
        user.setUserName(updatedUser.getUserName());
        user.setRole(updatedUser.getRole());
        if (userRepository.findByUserId(updatedUser.getUserId()).isEmpty()){
            user.setUserId(updatedUser.getUserId());
        }
        userRepository.save(user);

        if (user.getRole().toLowerCase().equals("student")){
            studentRepository.findByStudentId(userId)
                    .map(student -> {
                        student.setStudentId(user.getUserId());
                        student.setName(user.getUserName());
                        return studentRepository.save(student);
                    })
                    .orElseGet(() ->{
                        return studentRepository.save(new Student(user.getUserName(), user.getUserId(), user.getEmail()));
                    }
                    );
        }

        return "Success";

    }

    /**
     * Remove a user from database
     * 
     * @param email    email of requester
     * @param password password of requester
     * @param userId   user Id of person requester is deleting
     * @return "Success" if requester has permission and the user ID is found, error
     *         message otherwise
     */
    @DeleteMapping("/user/{userId}")
    @ResponseBody
    public String deleteUser(@RequestParam("email") String email, @RequestParam("password") String password,
            @PathVariable("userId") String userId) {
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADMIN_DELETE_USER, email, password, userRepository)) {
            return "Only Admin are allowed to delete user";
        }

        if (userRepository.findByUserId(userId).isEmpty()) {
            return "User not found";
        }
        userRepository.deleteByUserId(userId);
        studentRepository.deleteByStudentId(userId);
        return "Success";
    }

    /**
     * ADMIN -- COURSE API
     */

    /**
     * Retrieve list of courses
     * 
     * @param email
     * @param password
     */
    @GetMapping("/course")
    @ResponseBody
    public List<Course> getAllCourses(@RequestParam("email") String email, @RequestParam("password") String password) {
        if (PermissionUtils.hasPermission(PermissionsEnum.ALL_GET_COURSE, email, password, userRepository)) {
            return courseRepository.findAll();
        }
        return new ArrayList<>();
    }

    /**
     * Retrieve a course
     * 
     * @param email
     * @param password
     * @param courseId
     */
    @GetMapping("/course/{courseId}")
    @ResponseBody
    public Course getCourse(@RequestParam("email") String email, @RequestParam("password") String password,
            @PathVariable("courseId") String courseId) {
        if (PermissionUtils.hasPermission(PermissionsEnum.ALL_GET_COURSE, email, password, userRepository)) {
            return courseRepository.findByCourseId(courseId).orElse(null);
        }
        return null;

    }

    /**
     * Put a new course to the database
     * 
     * @param email
     * @param password
     * @param course
     * @return
     */
    @PostMapping("/course")
    @ResponseBody
    public String addCourse(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestBody Course course) {
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADMIN_ADD_COURSE, email, password, userRepository)) {
            return "Only Admin are allowed to add new course";
        }

        if (course.getCourseId() == null) {
            return "Course ID required to execute this action.";
        }

        if (courseRepository.findByCourseId(course.getCourseId()).isPresent()) {
            return "Existing course id";
        }

        courseRepository.save(course);
        return "Success";
    }

    /**
     * Update a course
     * 
     * @param email
     * @param password
     * @param updatedCourse
     * @param courseId
     * @return
     */
    @PutMapping("course/{courseId}")
    @ResponseBody
    public String updateCourse(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestBody Course updatedCourse, @PathVariable("courseId") String courseId) {
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADMIN_MODIFY_COURSE, email, password, userRepository)) {
            return "Only Admin are allowed to update course info";
        }

        if (courseRepository.findByCourseId(courseId).isEmpty()) {
            return "Course not found";
        }

        Course course = courseRepository.findByCourseId(courseId).get();

        course.setCourseName(updatedCourse.getCourseName());


        course.setSyllabus(updatedCourse.getSyllabus());
        course.setNumCredits(updatedCourse.getNumCredits());
        course.setCapacity(updatedCourse.getCapacity());

        course.setStartDate(updatedCourse.getStartDate());
        course.setEndDate(updatedCourse.getEndDate());
        // course.setSchedule(updatedCourse.getSchedule());
        courseRepository.save(course);
        return "Success";
    }

    /**
     * Remove a course from database
     * 
     * @param email
     * @param password
     * @param courseId
     * @return
     */
    @DeleteMapping("course/{courseId}")
    @ResponseBody
    public String deleteCourse(@RequestParam("email") String email, @RequestParam("password") String password,
            @PathVariable("courseId") String courseId) {
        if (!PermissionUtils.hasPermission(PermissionsEnum.ADMIN_DELETE_COURSE, email, password, userRepository)) {
            return "Only Admin are allowed to delete course";
        }

        if (courseRepository.findByCourseId(courseId).isEmpty()) {
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
    public String courseRegister(@RequestParam("email") String email, @RequestParam("password") String password,
            @PathVariable String courseId) {

        if (!PermissionUtils.hasPermission(PermissionsEnum.STUDENT_REGISTER_COURSE, email, password, userRepository)) {
            return "Only students are allowed to register course";
        }

//        // create student object
        String hashedPassword = SecurityUtils.hashPassword(password);
        Student student = studentRepository
                .findByStudentId(userRepository.findByEmailAndPassword(email, hashedPassword).get(0).getUserId()).get();
        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        String studentId = student.getStudentId();
//
//        // check capacity
        if (course.getStudentList().size() >= course.getCapacity()) {
            return "This course is already full";
        }

        // success, update database

        student.addCourse(course);
        studentRepository.save(student);


        return "You successfully register for this course";
    }

    /**
     * Cancel a course
     */
    @PutMapping("/course/cancel/{courseId}")
    public String courseCancel(@RequestParam("email") String email, @RequestParam("password") String password,
            @PathVariable String courseId) {

        if (!PermissionUtils.hasPermission(PermissionsEnum.STUDENT_CANCEL_COURSE, email, password, userRepository)) {
            return "You don't have permission to perform this action";
        }

        // create student object
        Student student = studentRepository
                .findByStudentId(userRepository.findByEmailAndPassword(email, password).get(0).getUserId()).get();
//        // check student in this course
        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        if (course == null) {
            return "Invalid course Id";
        }
        if (!course.getStudentList().contains(student)) {
            return "You are not in this course";
        }

        // check 2-week duration
        long milliToday = new Date().toInstant().toEpochMilli();
        Date startDay = new Date();
        try {
           startDay = new SimpleDateFormat("dd/mm/yyyy").parse(course.getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliStartDay = startDay.toInstant().toEpochMilli();
        long diff = milliToday - milliStartDay;
        if (diff > 14*24*60*60*1000){
            return "The course has been run for over 2 weeks and thus can not be cancelled";
        }

        // success, update database
        Student updateStudent = studentRepository.getOne(student.getId()); // does update student automatically update course
        Course updateCourse = courseRepository.getOne(course.getId());
        updateStudent.removeCourse(course);

        studentRepository.save(updateStudent);

        return "You successfully cancel this course";
    }

    /**
     * Edit student profile
     */
    @PutMapping("/profile/info/{studentId}")
    public String updateProfile(@RequestParam("email") String email, @RequestParam("password") String password,
                                @PathVariable("studentId") String studentId,
                                @RequestBody Student updateStudent){
        // student edit student profile
        if (PermissionUtils.hasPermission(PermissionsEnum.STUDENT_EDIT_PROFILE, email, password,userRepository)){
            Student student = studentRepository.findByStudentId(studentId).get(); 
            student.setAboutMe(updateStudent.getAboutMe());
            
            studentRepository.save(student);
            return "Success";
        }

        // admin edit student profile
        if (PermissionUtils.hasPermission(PermissionsEnum.ADMIN_EDIT_STUDENT_INFO, email, password, userRepository)){
            Student student = studentRepository.findByStudentId(studentId).get(); 
            student.setCurrentRegisteredCourse(updateStudent.getCurrentRegisteredCourse());
            student.setDob(updateStudent.getDob());
            student.setEmail(updateStudent.getEmail());
            student.setGradYear(updateStudent.getGradYear());
            student.setName(updateStudent.getName());
            student.setNumCredits(updateStudent.getNumCredits());
//            student.setPastCourses(updateStudent.getPastCourses());
            student.setStudentId(updateStudent.getStudentId());

            studentRepository.save(updateStudent);
            return "Success";
        }
        return "You don't have permission to perform this action";
    }

    /**
     * Get student's course list
     */
    @GetMapping("/student/course")
    public Set<Course> getStudentCourse(@RequestParam("email") String email, @RequestParam("password") String password){
        if(!PermissionUtils.hasPermission(PermissionsEnum.STUDENT_GET_MY_COURSE, email, password, userRepository)){
            return new HashSet<>();
        }
        Student student = studentRepository.findByStudentId(userRepository.findByEmailAndPassword(email, password).get(0).getUserId()).get(); 
        return student.getCurrentRegisteredCourse();
    }
}
