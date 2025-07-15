package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.GlobalHandler.CourseNotFound;
import com.smm.course_registration.Repository.course_Repository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class courseService {
    course_Repository course_repository;

    public courseService(course_Repository course_repository) {};


    public Course Define_Course(Course course) {
        return course_repository.save(course);
    }

    public List<Course> View_Courses() {
        return  course_repository.findAll();
    };

    public Course View_Course(long id, String name) {
        Course course = course_repository.findByCourseIdAndCourseName(id, name);
        if(course == null) {
            throw new CourseNotFound("Course: " + name +  "not found");
        }
        return course;
    }

    @Scheduled(cron =  "0 0 0 * * *")
    public void UpdateCourseStatus(){
        List<Course> courses = course_repository.findAll();

        for(Course course : courses){
            course.setAutoStatus();
            course_repository.save(course);
        }
    }

    public void Soft_Delete_Course(long id, String name) {
        Course course= course_repository.findByCourseIdAndCourseName(id, name);
        course.setStatus("Deleted");
        course_repository.save(course);
    }

    public void Register_For_Course(long id, String name, Student student) {
        Course course = course_repository.findByCourseIdAndCourseName(id, name);
        if(student.getCourseCount() <= 3) {
            course.setStudent(student);
            student.setCourse(course);
        }

    }
}
