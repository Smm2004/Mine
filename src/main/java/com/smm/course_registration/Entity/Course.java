package com.smm.course_registration.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smm.course_registration.Entity.audit.Auditable;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(value = {"students"}, allowSetters = false)
public class Course extends Auditable {
    private String courseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String courseType;
    private String instructorName;
    private String status;
    /** Course can be:
     *	1. Open
     *	2. Deleted
     *	3. Closed
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @ManyToMany(mappedBy = "courses",fetch = FetchType.EAGER)
    @JsonBackReference("course-students")
    @JsonIgnore
    private List<Student> students =  new ArrayList<>();


    public Course(){};

    public Course(String CourseName, LocalDate StartDate, LocalDate EndDate, String CourseType, String InstructorName){
        this.courseName = CourseName;
        this.startDate = StartDate;
        this.endDate = EndDate;
        this.courseType = CourseType;
        this.instructorName = InstructorName;
    }
    public void setCourseName(String name){
        this.courseName = name;
    }
    public String getCourseName(){
        return courseName;
    }

    public void setStartDate(LocalDate date){
        this.startDate = date;
    }
    public LocalDate getStartDate(){
        return startDate;
    }

    public void setEndDate(LocalDate date){
        this.endDate = date;
    }
    public LocalDate getEndDate(){
        return endDate;
    }

    public void setCourseType(String type){
        this.courseType = type;
    }
    public String getCourseType(){
        return courseType;
    }

    public void setInstructorName(String name){
        this.instructorName = name;
    }
    public String getInstructorName(){
        return instructorName;
    }

    public void setCourseId(long id){
        this.courseId = id;
    }
    public long getCourseId(){
        return courseId;
    }

    public void setAutoStatus(){
        LocalDate today = LocalDate.now();
        if(today.isAfter(startDate)){
            this.status = "Closed";
        }

    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }

    public void addStudent(Student student){
        if (!this.students.contains(student)) {
            this.students.add(student);
        }
    }
    public List<Student> getStudents(){
        return students;
    }

    public int getStudentCount(){
        if(students == null)
            return 0;

        return students.size();
    }
}