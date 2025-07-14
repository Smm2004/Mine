package Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student{
    private long N_ID;
    private String Name;
    private String PersonalPic;
    //String used as placeholder for object that will be used when I discover what to use
    private String Level;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Student_ID;

    @ManyToMany
    @JoinTable(
            name = "Student_Table",
            joinColumns = @JoinColumns(name = 'Student_ID'),
            inverseJoinColumns = @JoinColumns(name = 'Course_ID'))
    private List<Course> Course;

    public Student(){};

    public Student(long N_ID, String Name, String PersonalPic, String Level){
        this.N_ID = N_ID;
        this.Name = Name;
        this.PersonalPic = PersonalPic;
        this.Level = Level;
    }

    protected void SetN_ID(long id){
        this.N_ID = id;
    }
    protected long GetN_ID(){
        return N_ID;
    }

    protected void SetName(String name){
        this.Name = name;
    }
    protected String GetName(){
        return Name;
    }

    protected void SetPersonalPic(String pp){
        this.PersonalPic = pp;
    }
    protected String GetPersonalPic(){
        return PersonalPic;
    }

    protected void SetLevel(String level){
        this.Level = level;
    }
    protected String GetLevel(){
        return Level;
    }
}
