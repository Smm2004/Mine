package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.CourseRegistrationDTO;
import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Services.courseService;
import com.smm.course_registration.Services.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Courses")
public class courseController {

    courseService courseservice;
    emailService emailservice;
    @Autowired
    public courseController(courseService courseservice, emailService emailservice) {
        this.courseservice = courseservice;
        this.emailservice = emailservice;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/Define")
    public Course Define(@RequestBody Course course) {
        return courseservice.defineCourse(course);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/ViewAll")
    public List<Course> ViewAll() {
        return courseservice.viewCourses();
    }


    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/ViewIDandName")
    public Course ViewIDandName(@RequestParam String name, @RequestParam long id) {
        return courseservice.viewCourse(id, name);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/Register")
    public void Register(@RequestBody CourseRegistrationDTO dto) {
        courseservice.registerForCourse(dto.getId(), dto.getName(), dto.getNId());
        Student studentForEmail = courseservice.getStudentByNId(dto.getNId());
        emailservice.SendEmail(dto.getId(), dto.getName(), studentForEmail);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/Soft-Delete")
    public void Soft_Delete(@RequestParam long id, @RequestParam String name) {
        courseservice.softDeleteCourse(id, name);
    }

}
