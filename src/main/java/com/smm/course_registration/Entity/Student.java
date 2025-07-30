package com.smm.course_registration.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smm.course_registration.Entity.audit.Auditable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends Auditable {

    @Column(unique = true, nullable = false)
    private Long nId;
    private String name;
    private String email;
    private String personalPic;
    //String used as placeholder for object that will be used when I discover what to use
    private String level;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonManagedReference("course-students")
    private List<Course> courses =  new ArrayList<>();

    public Student(){};

    public Student(long N_ID, String Name, String email, String PersonalPic, String Level){
        this.nId = N_ID;
        this.name = Name;
        this.email = email;
        this.personalPic = PersonalPic;
        this.level = Level;
    }

    public void setNID(Long id){this.nId = id;}
    public Long getNID(){
        return nId;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPersonalPic(String pp){
        this.personalPic = pp;
    }
    public String getPersonalPic(){
        return personalPic;
    }

    public void setLevel(String level){
        this.level = level;
    }
    public String getLevel(){
        return level;
    }


    public void addCourse(Course course){
        if (!this.courses.contains(course)) {
            this.courses.add(course);
        }
    }
    public List<Course> getCourses(){
            return courses;
}


    public int getCourseCount() {
        if (this.courses == null) {
            return 0;
        }

        return (int) this.courses.stream()
                .filter(course -> course.getStatus() != null && "OPEN".equals(course.getStatus()))
                .count();
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){return this.email;}
}