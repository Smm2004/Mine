package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.CourseRegistrationDTO;
import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Entity.User;
import com.smm.course_registration.GlobalHandler.CourseNotFound;
import com.smm.course_registration.Repository.studentRepository;
import com.smm.course_registration.Repository.userRepository;
import com.smm.course_registration.Services.courseService;
import com.smm.course_registration.Services.emailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class courseController {

    courseService courseservice;
    emailService emailservice;
    studentRepository student_repository;
    userRepository user_repository;
    private static final Logger logger = LoggerFactory.getLogger(courseController.class); // Logger instance

    @Autowired
    public courseController(courseService courseservice, emailService emailservice,  studentRepository student_repository, userRepository user_repository) {
        this.courseservice = courseservice;
        this.emailservice = emailservice;
        this.student_repository = student_repository;
        this.user_repository = user_repository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/define")
    public ResponseEntity<String> Define(@RequestBody Course course) {
        try {
            Course definedCourse = courseservice.defineCourse(course);
            return new ResponseEntity<>("Course '" + definedCourse.getCourseName() + "' defined successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<>("Failed to define course: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/viewall")
    public List<Course> ViewAll() {
        return courseservice.viewCourses();
    }


    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/viewidandname")
    public Course ViewIDandName(@RequestParam String name, @RequestParam long id) {
        return courseservice.viewCourse(id, name);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody CourseRegistrationDTO dto) {
        try {
            Optional<Student> studentOptional = student_repository.findByNId(dto.getNId());

            if (studentOptional.isEmpty()) {
                throw new IllegalArgumentException("Student with NID " + dto.getNId() + " not found.");
            }
            Student student = studentOptional.get();
            courseservice.registerForCourse(dto.getId(), dto.getNId());
            Student studentForEmail = courseservice.getStudentByNId(dto.getNId());
            emailservice.SendEmail(dto.getId(), studentForEmail);
            return new ResponseEntity<>("Course registration successful for student NID " + dto.getNId()  + "!", HttpStatus.OK);

        } catch (CourseNotFound e) {
            // If the course isn't found, return 404 Not Found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            // If the student isn't found or other bad arguments, return 400 Bad Request
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Catch any other unexpected exceptions and return 500 Internal Server Error
            e.printStackTrace(); // Log the full stack trace for debugging
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/soft-delete")
    public void Soft_Delete(@RequestParam long id, @RequestParam String name) {
        courseservice.softDeleteCourse(id, name);
    }

}
