package Entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@Where(clause = "status = 'Open'")
public class Course{
    private String CourseName;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private String CourseType;
    private String InstructorName;
    private String Status;
    /** Course can be:
     *  1. Not Open yet
     *	2. Open
     *	3. Full
     *	4. Deleted
     *	5. Closed
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Course_ID;

    @ManyToMany
    @JoinTable(
            name = "Course_Student_Table",
            joinColumns = @JoinColumn(name = "Course_ID"),
            inverseJoinColumns = @JoinColumn(name = "Student_ID"))
    private List<Student> students;

    private LocalDate today = LocalDate.now();

    public Course(){};

    public Course(String courseName, LocalDate startDate, LocalDate endDate, String courseType, String instructorName){
        this.CourseName = courseName;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.CourseType = courseType;
        this.InstructorName = instructorName;
        this.SetAutoStatus();


    }
    public void SetCourseName(String name){
        this.CourseName = name;
    }
    public String GetCourseName(){
        return CourseName;
    }

    public void SetStartDate(LocalDate date){
        this.StartDate = date;
    }
    public LocalDate GetStartDate(){
        return StartDate;
    }

    public void SetEndDate(LocalDate date){
        this.EndDate = date;
    }
    public LocalDate GetEndDate(){
        return EndDate;
    }

    public void SetCourseType(String type){
        this.CourseType = type;
    }
    public String GetCourseType(){
        return CourseType;
    }

    public void SetInstructorName(String name){
        this.InstructorName = name;
    }
    public String GetInstructorName(){
        return InstructorName;
    }

    public void SetAutoStatus(){
        if(today.isBefore(StartDate)){
            this.Status = "Not Open Yet";
        }
        if(today.isAfter(StartDate) && today.isBefore(EndDate)){
            this.Status = "Open";
        }
        if(today.isAfter(EndDate)){
            this.Status = "Closed";
        }
    }
    public void SetStatus(String status){
        this.Status = status;
    }
    public String GetStatus(){
        return Status;
    }

    public void SetStudent(Student student){
        this.students.add(student);
    }
}


