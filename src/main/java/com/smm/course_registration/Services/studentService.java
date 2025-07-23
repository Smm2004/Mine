package com.smm.course_registration.Services;

import com.smm.course_registration.DTO.StudentRegistrationDTO;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Entity.User;
import com.smm.course_registration.GlobalHandler.StudentNotFound;
import com.smm.course_registration.Repository.studentRepository;
import com.smm.course_registration.Repository.userRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class studentService {

    studentRepository student_repository;
    userRepository user_repository;
    PasswordEncoder passwordEncoder;

    public studentService(studentRepository student_repository, userRepository user_repository,PasswordEncoder passwordEncoder) {

        this.student_repository = student_repository;
        this.user_repository = user_repository;
        this.passwordEncoder = passwordEncoder;
    };

    @Transactional
    public Student registerStudent(StudentRegistrationDTO registrationDTO) {
        if (user_repository.findByUsername(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        if (student_repository.findByNId(registrationDTO.getnId()).isPresent()) {
            throw new IllegalArgumentException("Student with this National ID already exists.");
        }

        if (student_repository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Student with this email already exists.");
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newUser.setRole("ROLE_STUDENT");
        user_repository.save(newUser);

        Student newStudent = new Student();
        newStudent.setName(registrationDTO.getName());
        newStudent.setLevel(registrationDTO.getLevel());
        newStudent.setNID(registrationDTO.getnId());
        newStudent.setEmail(registrationDTO.getEmail());
        student_repository.save(newStudent);

        return newStudent;
    }


    public Page<Student> viewStudents(int page, int size, String sortby, String sortdirection){
        Sort sort = Sort.by(sortdirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
        Pageable pageable = PageRequest.of(page, size, sort);
        return student_repository.findAll(pageable);
    }

    @Transactional
    public Page<Student> viewStudent(String name, int page, int size, String sortby, String sortdirection) {

        Sort sort = Sort.by(sortdirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
        Pageable pageable = PageRequest.of(page, size, sort);
        return student_repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void updateStudentPersonalPic(Long studentId, String newPicFileName) {
        Student student = student_repository.findByNId(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
        student.setPersonalPic(newPicFileName);
        student_repository.save(student);
    }

}