package com.smm.course_registration.GlobalHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler(){};

    @ExceptionHandler(CourseNotFound.class)
    public ResponseEntity<String> handleCourseNotFound(CourseNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFound.class)
    public ResponseEntity<String> handleStudentNotFound(StudentNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
//The

}
