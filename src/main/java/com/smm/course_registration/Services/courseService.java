package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Entity.User;
import com.smm.course_registration.Enum.CourseStatus;
import com.smm.course_registration.GlobalHandler.CourseNotFound;
import com.smm.course_registration.Repository.courseRepository;
import com.smm.course_registration.Repository.studentRepository;
import com.smm.course_registration.Repository.userRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class courseService {

    private static final Logger logger = LoggerFactory.getLogger(courseService.class);

    courseRepository course_repository;
    studentRepository student_repository;
    userRepository user_repository;


    public courseService(courseRepository course_repository, studentRepository student_repository, userRepository user_repository) {
        this.course_repository = course_repository;
        this.student_repository = student_repository;
        this.user_repository = user_repository;
    }

    ;

    public Student getStudentByNId(long nId) {
        return student_repository.findByNId(nId).orElseThrow(() -> new EntityNotFoundException("Student with NID " + nId + " not found."));
    }


    @Transactional
    public Course defineCourse(Course course) {
        return course_repository.save(course);
    }

    public List<Course> viewCourses() {
        return course_repository.findAll();
    }

    public Page<Course> viewCourses(int page, int size, String sortby, String sortdirection){
        Sort sort = Sort.by(sortdirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
        Pageable pageable = PageRequest.of(page, size, sort);
        return course_repository.findAll(pageable);
    }

    public Course viewCourse(long id, String name) {
        Course course = course_repository.findByCourseIdAndCourseName(id, name);
        if (course == null) {
            throw new CourseNotFound("Course: " + name + "not found");
        }
        return course;
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void UpdateCourseStatus() {
        List<Course> courses = course_repository.findAll();
        for (Course course : courses) {
            course.setAutoStatus();
            if(course.getStatus().equals("CLOSED")){
                course_repository.delete(course);
            }
            course_repository.save(course);
        }


    }

    @Transactional
    public void softDeleteCourse(long id, String name) {
        Course course = course_repository.findByCourseIdAndCourseName(id, name);
        if (course == null) {
            throw new CourseNotFound("Course: " + name + "not found");
        }
        if (course.getStudentCount() == 0) {
            course.setStatus("DELETED");
            course_repository.save(course);
        } else if (course.getStudentCount() > 0) {
            throw new CourseNotFound(name + " has students registered");
        }
    }

    @Transactional
    public void registerForCourse(long courseId, long studentNId) {
        logger.debug("Attempting to register student NID {} for course ID {}.", studentNId, courseId);

        // 1. Find the Course by ID
        Course course = course_repository.findByCourseId(courseId);
        if (course == null) {
            throw new CourseNotFound("Course with ID " + courseId + " not found.");
        }
        if (course.getStatus().equals("CLOSED")) {
            throw new IllegalArgumentException("Course '" + course.getCourseName() + "' is closed and cannot be registered for.");
        }
        if (course.getStatus().equals("DELETED")) {
            throw new IllegalArgumentException("Course '" + course.getCourseName() + "' is deleted and cannot be registered for.");
        }

        // 2. Find the Student
        Optional<Student> studentOptional = student_repository.findByNId(studentNId);
        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("Student with NID " + studentNId + " not found.");
        }
        Student student = studentOptional.get();

        // The authentication (login status and role check) is handled by the JWT filter and @PreAuthorize
        // before this method is called. If we reach here, the user is authenticated and authorized.
        // Therefore, the explicit 'isLoggedIn' check on the User entity is no longer needed.

        // 3. Check course count limit
        if (student.getCourseCount() >= 3) {
            throw new IllegalArgumentException("Student " + student.getName() + " cannot register for more than 3 courses.");
        }

        if (student.getCourses().contains(course)) {
            logger.info("Student {} (NID: {}) is already registered for course {} (ID: {}). Skipping registration.",
                    student.getName(), studentNId, course.getCourseName(), courseId);
            throw new IllegalArgumentException("Student " + student.getName() + " is already registered for " + course.getCourseName() + ".");
        }

        course.addStudent(student);
        student.addCourse(course);

        course_repository.save(course);
        student_repository.save(student);
        logger.info("Student {} (NID: {}) successfully registered for course {} (ID: {}).",
                student.getName(), studentNId, course.getCourseName(), courseId);
    }
}