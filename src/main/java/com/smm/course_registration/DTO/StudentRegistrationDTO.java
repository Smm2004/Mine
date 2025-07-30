package com.smm.course_registration.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public class StudentRegistrationDTO {

    @NotBlank(message = "email(username) cannot be blank")
    @Email(message = "Please make sure that email is valid")
    private String email;

    @NotBlank(message = "password cannot be blank")
    private String password;

    @NotBlank(message = "national id cannot be blank")
    private long nId;

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "level cannot be blank")
    private String level;


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public long getnId() {
        return nId;
    }
    public void setnId(long nId) {
        this.nId = nId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

}
