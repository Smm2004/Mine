package com.smm.course_registration.DTO;

public class StudentDTO {
    private Long nId;
    private String name;
    private String email;
    private String personalPic; // This will hold the filename
    private String level;

    // Constructors
    public StudentDTO() {}

    public StudentDTO(Long nId, String name, String email, String personalPic, String level) {
        this.nId = nId;
        this.name = name;
        this.email = email;
        this.personalPic = personalPic;
        this.level = level;
    }

    // Getters and Setters
    public Long getNId() {
        return nId;
    }

    public void setNId(Long nId) {
        this.nId = nId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalPic() {
        return personalPic;
    }

    public void setPersonalPic(String personalPic) {
        this.personalPic = personalPic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

