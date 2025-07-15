package Service;

import Entity.Course;
import Entity.Student;
import GlobalHandler.CourseNotFound;
import Repository.Course_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Service
public class CourseService {
    Course_Repository course_repository;

    public CourseService(Course_Repository course_repository) {};


    public Course Define_Course(Course course) {
        return course_repository.save(course);
    }

    public List<Course> View_Courses() {
        return  course_repository.findAll();
    };

    public Course View_Course(long id, String name) {
        Course course = course_repository.findByCourseIdandCourseName(id, name);
        if(course == null) {
            throw new CourseNotFound("Course: " + name +  "not found");
        }
        return course;
    }

    @Scheduled(cron =  "0 0 0 * * *")
    public void UpdateCourseStatus(){
        List<Course> courses = course_repository.findAll();

        for(Course course : courses){
            course.SetAutoStatus();
            course_repository.save(course);
        }
    }

    public void Soft_Delete_Course(long id, String name) {
        Course course= course_repository.findByCourseIdandCourseName(id, name);
        course.SetStatus("Deleted");
        course_repository.save(course);
    }

    public void Register_For_Course(long id, String name, Student student) {
        Course course = course_repository.findByCourseIdandCourseName(id, name);
        if(student.GetCourseCount() <= 3) {
            course.SetStudent(student);
            student.SetCourse(course);
        }

    }
}
