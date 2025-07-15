package Service;

import Entity.Course;
import Entity.Student;
import Repository.Student_Repository;
import java.util.List;


@Service
public class StudentService {

    Student_Repository student_repository;

    public StudentService(Student_Repository student_repository) {
        this.student_repository = student_repository;
    };

    public List<Student> viewStudents(){
        return student_repository.findAll();
    }

    public Student viewStudent(int Nid){
        return student_repository.findByN_ID(Nid);
    }

}