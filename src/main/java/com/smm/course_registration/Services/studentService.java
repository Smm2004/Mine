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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.ErrorManager;


@Service
public class studentService {

    studentRepository student_repository;
    userRepository user_repository;
    PasswordEncoder passwordEncoder;
    FileStorageService fileStorageService;

    public studentService(studentRepository student_repository, userRepository user_repository,PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.student_repository = student_repository;
        this.user_repository = user_repository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    };

    @Transactional
    public void registerStudent(StudentRegistrationDTO registrationDTO, MultipartFile file) {
        try{
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
            String name = fileStorageService.storeFile(file);
            newStudent.setPersonalPic(name);
            student_repository.save(newStudent);

        } catch (Exception ex) {
        }
    }


    public Page<Student> viewStudents(int page, int size, String sortby, String sortdirection){
        Sort sort = Sort.by(sortdirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
        Pageable pageable = PageRequest.of(page, size, sort);
        return student_repository.findAll(pageable);
    }

    public Page<Student> viewStudent(String name, int page, int size, String sortby, String sortdirection) {

        Sort sort = Sort.by(sortdirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
        Pageable pageable = PageRequest.of(page, size, sort);
        return student_repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void updateStudentPersonalPic(MultipartFile file, long studentId) {
        try{
            Student student = student_repository.findByNId(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

            String fileName = fileStorageService.storeFile(file);

            student.setPersonalPic(fileName);
            student_repository.save(student);
        } catch (IOException ex) {

        } catch (Exception ex) {

        }
        }

    }
