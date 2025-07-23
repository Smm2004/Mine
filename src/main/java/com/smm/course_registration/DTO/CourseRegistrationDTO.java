package com.smm.course_registration.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.course_registration.Entity.Student;

public class CourseRegistrationDTO {

    private long id;
    @JsonProperty("nId")
    private long nId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNId() {
        return nId;
    }

    public void setNId(long id) {this.nId = id;
    }

}
