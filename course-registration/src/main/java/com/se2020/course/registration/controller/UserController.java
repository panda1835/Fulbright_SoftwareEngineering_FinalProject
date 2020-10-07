package com.se2020.course.registration.controller;

import java.util.*;

import com.se2020.course.registration.entity.Course;
import com.se2020.course.registration.entity.Student;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.repository.CourseRepository;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    /**
     * All user browse course
     */
    @GetMapping("/browse")
    public List<Course> browseCourse(){
        return courseRepository.findAll();
    }

    // ADMIN


    // STUDENT
    /**
     * Register for a course
     */
    @PutMapping("/course/register/{courseId}")
    public String courseRegister(@RequestBody String userId, @PathVariable String courseId){
        
        // check userId to be student
        List<User> user = userRepository.findByUserId(userId);
        if (user.size() == 0){
            return "InvalId user Id";
        }
        String role = user.get(0).getRole();
        if (role.compareTo("student") != 0){
            return "Only student can access this page";
        }
 
        // create student object
        Student student = studentRepository.findByStudentId(userId).get(0);


        Course course = courseRepository.findByCourseId(courseId).get(0);
        String studentId = student.getStudentId();
        
        // check capacity
        if (course.getCurrentCapacity() < course.getCapacity()){
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
        List<User> user = userRepository.findByUserId(userId);
        if (user.size() == 0){
            return "InvalId user Id";
        }
        String role = user.get(0).getRole();
        if (role.compareTo("student") != 0){
            return "Only student can access this page";
        }

        // create student object
        Student student = studentRepository.findByStudentId(userId).get(0);
        // check student in this course
        List<Course> courses = courseRepository.findByCourseId(courseId);
        if (courses.size() == 0){
            return "InvalId course Id";
        }
        Course course = courses.get(0);
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
