package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.GlobalHandler.StudentNotFound;
import com.smm.course_registration.Repository.studentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class studentService {

    studentRepository student_repository;

    public studentService(studentRepository student_repository) {
        this.student_repository = student_repository;
    };

    public List<Student> viewStudents(){
        return student_repository.findAll();
    }

    @Transactional
    public Student viewStudent(long Nid){
        Student student = student_repository.findByNId(Nid).orElseThrow(() -> new EntityNotFoundException("Student with NID " + Nid + " not found."));;
        if(student == null) {
            throw new StudentNotFound("Student with ID: " + Nid + "not found");
        }
        return student;
    }

}