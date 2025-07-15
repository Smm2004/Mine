package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.CourseRegistrationDTO;
import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Services.courseService;
import com.smm.course_registration.Services.emailService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/Define")
    public void Define(@RequestBody Course course) {
        courseservice.Define_Course(course);
    }

    @GetMapping("/ViewAll")
    public List<Course> ViewAll() {
        return courseservice.View_Courses();
    }

    @GetMapping("/ViewIDandName")
    public Course ViewIDandName(@RequestParam String name, @RequestParam long id) {
        return courseservice.View_Course(id, name);
    }

    @PostMapping("/Register")
    public void Register(@RequestBody CourseRegistrationDTO dto) {
        courseservice.Register_For_Course(dto.getId(), dto.getName(), dto.getStudent());
        emailservice.SendEmail(dto.getId(), dto.getName(),  dto.getStudent());
    }

    @PutMapping("/Soft-Delete")
    public void Soft_Delete(@RequestParam long id, @RequestParam String name) {
        courseservice.Soft_Delete_Course(id, name);
    }

}
