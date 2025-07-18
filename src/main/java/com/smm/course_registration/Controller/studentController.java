package com.smm.course_registration.Controller;

import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Services.studentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Student")
public class studentController {

    studentService studentservice;

    @Autowired
    public studentController(studentService studentservice) {
        this.studentservice = studentservice;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Views")
    public List<Student> views(){
        return studentservice.viewStudents();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/View/{Nid}")
    public Student view(@PathVariable long Nid){
        return studentservice.viewStudent(Nid);
    }

}
