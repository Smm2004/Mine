package com.smm.course_registration.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student{
    private long NID;
    private String Name;
    private String Email;
    private String personalPic;
    //String used as placeholder for object that will be used when I discover what to use
    private String Level;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long student_Id;

    @ManyToMany
    @JoinTable(
            name = "Student_Course_Table",
            joinColumns = @JoinColumn(name = "studentID"),
            inverseJoinColumns = @JoinColumn(name = "courseID"))
    private List<Course> Course;

    public Student(){};

    public Student(long N_ID, String Name, String email, String PersonalPic, String Level){
        this.NID = N_ID;
        this.Name = Name;
        this.Email = email;
        this.personalPic = PersonalPic;
        this.Level = Level;
    }

    public void setNID(long id){
        this.NID = id;
    }
    public long getNID(){
        return NID;
    }

    public void setName(String name){
        this.Name = name;
    }
    public String getName(){
        return Name;
    }

    public void setpersonalPic(String pp){
        this.personalPic = pp;
    }
    public String getpersonalPic(){
        return personalPic;
    }

    public void setLevel(String level){
        this.Level = level;
    }
    public String getLevel(){
        return Level;
    }

    public void setCourse(Course course){
        Course.add(course);
    }

    public int getCourseCount(){
        int num = 0;
        for(Course c : Course){
            if(c.getStatus() == "Open"){
                num++;
            }
        }
        return num;

    }

    public void setEmail(String email){
        this.Email = email;
    }
    public String getEmail(){return this.Email;}
}
