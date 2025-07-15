package com.smm.course_registration.Controller;

import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Services.studentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/Views")
    public List<Student> views(){
        return studentservice.viewStudents();
    }

    @GetMapping("/View/{Nid}")
    public Student view(@PathVariable int Nid){
        return studentservice.viewStudent(Nid);
    }

}
