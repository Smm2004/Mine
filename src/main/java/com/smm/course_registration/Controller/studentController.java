package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.StudentRegistrationDTO;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Repository.studentRepository;
import com.smm.course_registration.Services.studentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class studentController {

    private static final Logger logger = LoggerFactory.getLogger(studentController.class);

    studentService studentservice;
    studentRepository studentrepository;

    @Autowired
    public studentController(studentService studentservice, studentRepository studentrepository) {
        this.studentservice = studentservice;
        this.studentrepository = studentrepository;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody StudentRegistrationDTO registrationDTO) {
        try {
            studentservice.registerStudent(registrationDTO);
            return new ResponseEntity<>("Student registered successfully!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/views")
    public ResponseEntity<Page<Student>> views(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "studentId") String sortby,
            @RequestParam(defaultValue = "asc") String sortdirection
    ){
        Page page1 = studentservice.viewStudents(page, size, sortby, sortdirection);
        return new ResponseEntity<>(page1, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view")
    public ResponseEntity<Page<Student>> views(
            @RequestParam String name,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "studentId") String sortby,
            @RequestParam(defaultValue = "asc") String sortdirection
    ){
        Page page1 = studentservice.viewStudent(name, page, size, sortby, sortdirection);
        return new ResponseEntity<>(page1, HttpStatus.OK);
    }

    @PutMapping("/{studentId}/update-pic")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<?> updateStudentProfilePic(@PathVariable Long studentId, @RequestBody String newPicFileName) {
        try {
            studentservice.updateStudentPersonalPic(studentId, newPicFileName);
            return ResponseEntity.ok("Student " + studentId + " profile picture updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating student profile picture: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error updating profile picture: " + e.getMessage());
        }
    }



}
