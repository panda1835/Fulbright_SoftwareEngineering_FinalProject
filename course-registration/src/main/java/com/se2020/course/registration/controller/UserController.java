package com.se2020.course.registration.controller;

import java.util.*;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.Student;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.repository.CourseRepository;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private UserRepository userRepository;

    //ALL USERS
    @GetMapping("/browse")
    public List<Course> browseCourse(){
        return courseRepository.findAll();
    }

    // ADMIN


    // STUDENT
    /**
     * Register for a course
     */
    @PutMapping("/register/{courseId}")
    public String cancelCourse(@RequestBody Student student, @PathVariable String courseId){
        Course course = courseRepository.findById(courseId);
        String studentId = student.getStudentId();
        
        // check capacity
        if (course.getCurrentCapacity() < course.getCapacity()){
            return "This course is already full";
        }

        // check student register status
        for (String id: course.getStudentList()){
            if (id.compareTo(studentId) == 0){
                return "You already registered for this course";
            }
        }

        // check prerequisite
        List<String> prerequisite = course.getPrerequisite();
        List<String> pastCourse = student.getPastCourses();
        for (String pre: prerequisite){
            for (int i = 0; i < pastCourse.size(); i++){
                if (pastCourse.get(i).compareTo(pre) == 0) {
                    continue;
                }
            }
            return "You are not fulfill the prerequisite";
        }

        // success, update database
        Student updateStudent = studentRepository.getOne(studentId);
        Student updateCourse = courseRepository.getOne(courseId);
        updateStudent.getCurrentRegisteredCourse().add(courseId);
        updateCourse.getStudentList().add(studentId);
        studentRepository.save(updateStudent);
        courseRepository.save(updateCourse);

        return "You successfully register for this course";
    }

    /**
     * Student Edit profile
     */
    @PutMapping("/profile/edit/{role}/{studentId}")
    public String updateProfile(@RequestBody Student student, @PathVariable String role
                                                            , @PathVariable String studentId){
        // check student
        if (role.compareTo("student") == 0){
            Student updateStudent = studentRepository.getOne(studentId);
            updateStudent.setAboutMe(student.getAboutMe());
            updateStudent.setCurrentRegisteredCourse(student.getCurrentRegisteredCourse());
            updateStudent.setDob(student.getDob());
            updateStudent.setEmail(student.getDob());
            updateStudent.setGradYear(student.getGradYear());
            updateStudent.setName(student.getName());
            updateStudent.setNumCredits(student.getNumCredits());
            updateStudent.setPastCourses(student.getPastCourses());
            updateStudent.setStudentId(student.getStudentId());
            studentRepository.save(updateStudent);
        }
        return "Success";
    }
}
