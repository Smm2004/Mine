package com.smm.course_registration.GlobalHandler;

public class CourseNotFound extends RuntimeException{

    public CourseNotFound(String message) {
        super(message);
    }

}
