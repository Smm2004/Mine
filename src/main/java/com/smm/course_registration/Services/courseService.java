package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.GlobalHandler.CourseNotFound;
import com.smm.course_registration.Repository.courseRepository;
import com.smm.course_registration.Repository.studentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class courseService {

    courseRepository course_repository;
    studentRepository student_repository;

    public courseService(courseRepository course_repository, studentRepository student_repository) {
        this.course_repository = course_repository;
        this.student_repository = student_repository;
    };

    public Student getStudentByNId(long nId) {
        return student_repository.findByNId(nId).orElseThrow(() -> new EntityNotFoundException("Student with NID " + nId + " not found."));
    }


    @Transactional
    public Course defineCourse(Course course) {


        return course_repository.save(course);
    }

    public List<Course> viewCourses() {
        return  course_repository.findAll();
    };

    public Course viewCourse(long id, String name) {
        Course course = course_repository.findByCourseIdAndCourseName(id, name);
        if(course == null) {
            throw new CourseNotFound("Course: " + name +  "not found");
        }
        return course;
    }

    @Transactional
    @Scheduled(cron =  "0 0 0 * * *")
    public void UpdateCourseStatus(){
        List<Course> courses = course_repository.findAll();

        for(Course course : courses){
            course.setAutoStatus();
            course_repository.save(course);
        }
    }

    @Transactional
    public void softDeleteCourse(long id, String name) {
        Course course= course_repository.findByCourseIdAndCourseName(id, name);

        if(course == null) {
            throw new CourseNotFound("Course: " + name +  "not found");
        }
        course.setStatus("Deleted");
        course_repository.save(course);
    }
    @Transactional

    public void registerForCourse(long courseId, String courseName, long studentNId) {

        Course course = course_repository.findByCourseIdAndCourseName(courseId, courseName);
        if(course == null) {
            throw new CourseNotFound("Course: " + courseName +  " not found");
        }

        Optional<Student> studentOptional = student_repository.findByNId(studentNId);

        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("Student with NID " + studentNId + " not found.");
        }
        Student student = studentOptional.get();

        if(student.getCourseCount() < 3) {
            if (!student.getCourses().contains(course)) {
                course.setStudent(student);
                student.setCourse(course);
            } else {
                System.out.println("Student " + student.getName() + " is already registered for " + course.getCourseName());
                return;
            }



            course_repository.save(course);
            student_repository.save(student);

        }
        else {
            throw new IllegalArgumentException("Student " + student.getName() + " cannot register for more than 3 courses.");
        }
    }
}
