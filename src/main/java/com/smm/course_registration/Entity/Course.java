package com.smm.course_registration.Entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;

@Entity
@Where(clause = "status = 'Open'")
public class Course{
    private String courseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String courseType;
    private String instructorName;
    private String status;
    /** Course can be:
     *  1. Not Open yet
     *	2. Open
     *	3. Full
     *	4. Deleted
     *	5. Closed
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;

    @ManyToMany
    @JoinTable(
            name = "Course_Student_Table",
            joinColumns = @JoinColumn(name = "Course_ID"),
            inverseJoinColumns = @JoinColumn(name = "Student_ID"))
    private List<Student> students;

    private LocalDate today = LocalDate.now();

    public Course(){};

    public Course(String CourseName, LocalDate StartDate, LocalDate EndDate, String CourseType, String InstructorName){
        this.courseName = CourseName;
        this.startDate = StartDate;
        this.endDate = EndDate;
        this.courseType = CourseType;
        this.instructorName = InstructorName;
        this.setAutoStatus();


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

    public void setAutoStatus(){
        if(today.isBefore(startDate)){
            this.status = "Not Open Yet";
        }
        if(today.isAfter(startDate) && today.isBefore(endDate)){
            this.status = "Open";
        }
        if(today.isAfter(endDate)){
            this.status = "Closed";
        }
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }

    public void setStudent(Student student){
        this.students.add(student);
    }
}


