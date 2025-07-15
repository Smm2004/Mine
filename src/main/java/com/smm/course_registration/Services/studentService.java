package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.GlobalHandler.StudentNotFound;
import com.smm.course_registration.Repository.student_Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class studentService {

    student_Repository student_repository;

    public studentService(student_Repository student_repository) {
        this.student_repository = student_repository;
    };

    public List<Student> viewStudents(){
        return student_repository.findAll();
    }

    public Student viewStudent(int Nid){
        Student student = student_repository.findByNID(Nid);
        if(student == null) {
            throw new StudentNotFound("Student with ID: " + Nid + "not found");
        }
        return student;
    }

}