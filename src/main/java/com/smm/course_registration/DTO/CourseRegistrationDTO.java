package com.smm.course_registration.DTO;

import com.smm.course_registration.Entity.Student;

public class CourseRegistrationDTO {

    private long id;
    private String name;
    private long nId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNId() {
        return nId;
    }

    public void setNId(long id) {this.nId = id;
    }

}
