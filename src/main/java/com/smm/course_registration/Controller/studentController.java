package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.StudentRegistrationDTO;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Repository.studentRepository;
import com.smm.course_registration.Services.FileStorageService;
import com.smm.course_registration.Services.studentService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class studentController {

    private static final Logger logger = LoggerFactory.getLogger(studentController.class);

    studentService studentservice;
    studentRepository studentrepository;
    FileStorageService fileStorageService;

    @Autowired
    public studentController(studentService studentservice, studentRepository studentrepository, FileStorageService fileStorageService) {
        this.studentservice = studentservice;
        this.studentrepository = studentrepository;
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "View all students and their information, only admin can access")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/views")
    public ResponseEntity<Page<Student>> views(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "studentId") String sortby,
            @RequestParam(defaultValue = "asc") String sortdirection
    ) {
        Page page1 = studentservice.viewStudents(page, size, sortby, sortdirection);
        return new ResponseEntity<>(page1, HttpStatus.OK);
    }

    @Operation(summary = "View one student and his info, only admin can access")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view")
    public ResponseEntity<Page<Student>> views(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "studentId") String sortby,
            @RequestParam(defaultValue = "asc") String sortdirection
    ) {
        Page page1 = studentservice.viewStudent(name, page, size, sortby, sortdirection);
        return new ResponseEntity<>(page1, HttpStatus.OK);
    }

    @Operation(summary = "Update student profile pic")
    @PutMapping("/{studentId}/update-pic")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<?> updateStudentProfilePic(@PathVariable Long studentId, @RequestParam("file") MultipartFile file) {
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }
            studentservice.updateStudentPersonalPic(file, studentId);
            return ResponseEntity.ok("Student " + studentId + " profile picture updated successfully.");

    }
}