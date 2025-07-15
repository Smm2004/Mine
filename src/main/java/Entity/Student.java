package Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student{
    private long N_ID;
    private String Name;
    private String Email;
    private String PersonalPic;
    //String used as placeholder for object that will be used when I discover what to use
    private String Level;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Student_ID;

    @ManyToMany
    @JoinTable(
            name = "Student_Course_Table",
            joinColumns = @JoinColumn(name = "Student_ID"),
            inverseJoinColumns = @JoinColumn(name = "Course_ID"))
    private List<Course> Course;

    public Student(){};

    public Student(long N_ID, String Name, String email, String PersonalPic, String Level){
        this.N_ID = N_ID;
        this.Name = Name;
        this.Email = email;
        this.PersonalPic = PersonalPic;
        this.Level = Level;
    }

    public void SetN_ID(long id){
        this.N_ID = id;
    }
    public long GetN_ID(){
        return N_ID;
    }

    public void SetName(String name){
        this.Name = name;
    }
    public String GetName(){
        return Name;
    }

    public void SetPersonalPic(String pp){
        this.PersonalPic = pp;
    }
    public String GetPersonalPic(){
        return PersonalPic;
    }

    public void SetLevel(String level){
        this.Level = level;
    }
    public String GetLevel(){
        return Level;
    }

    public void SetCourse(Course course){
        Course.add(course);
    }

    public int GetCourseCount(){
        int num = 0;
        for(Course c : Course){
            if(c.GetStatus() == "Open"){
                num++;
            }
        }
        return num;
    }

    public void SetEmail(String email){
        this.Email = email;
    }
    public String GetEmail(){return this.Email;}
}
