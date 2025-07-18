package com.smm.course_registration.Repository;

import com.smm.course_registration.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface courseRepository extends JpaRepository<Course,Integer> {
    List<Course> findByCourseName(String courseName);
    Course findByCourseId(long course_Id);
    List<Course> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
    List<Course> findByCourseType(String courseType);
    List<Course> findByInstructorName(String instructorName);
    List<Course> findByStatus(String status);
    Course findByCourseIdAndCourseName(long course_ID, String courseName);
}