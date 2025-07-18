package com.smm.course_registration.GlobalHandler;

public class StudentNotFound extends RuntimeException {

    public StudentNotFound(){};
    public StudentNotFound(String message) {
        super(message);
    }
}
