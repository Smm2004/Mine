package com.smm.course_registration.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.course_registration.Entity.Student;
import jakarta.validation.constraints.NotBlank;

public class CourseRegistrationDTO {

    @NotBlank(message = "Please input course id")
    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
