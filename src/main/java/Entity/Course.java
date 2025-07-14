package Entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Where(clause = "status = 'Open'")
public class Course{
    private String CourseName;
    private String StartDate;
    private String EndDate;
    private String CourseType;
    private String InstructorName;
    private String Status;
    /** Course can be:
     *   1. Not Open yet
     *	2. Open
     *	3. Full
     *	4. Deleted
     *	5. Closed
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Course_ID;

    @ManyToMany
    private List<Student> students;


    public Course(){};

    public Course(String courseName, String startDate, String endDate, String courseType, String instructorName, String status){
        this.CourseName = courseName;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.CourseType = courseType;
        this.InstructorName = instructorName;
        this.Status = status;
    }

    protected void SetCourseName(String name){
        this.CourseName = name;
    }
    protected String GetCourseName(){
        return CourseName;
    }

    protected void SetStartDate(String date){
        this.StartDate = date;
    }
    protected String GetStartDate(){
        return StartDate;
    }

    protected void SetEndDate(String date){
        this.EndDate = date;
    }
    protected String GetEndDate(){
        return EndDate;
    }

    protected void SetCourseType(String type){
        this.CourseType = type;
    }
    protected String GetCourseType(){
        return CourseType;
    }

    protected void SetInstructorName(String name){
        this.InstructorName = name;
    }
    protected String GetInstructorName(){
        return InstructorName;
    }

    protected void SetStatus(String status){
        this.Status = status;
    }
    protected String GetStatus(){
        return Status;
    }

}


