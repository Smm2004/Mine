package Controller;

import Entity.Student;
import Repository.Student_Repository;
import Service.EmailService;
import Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Student")
public class StudentController {

    StudentService studentservice;

    @Autowired
    public StudentController(StudentService studentservice) {
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
