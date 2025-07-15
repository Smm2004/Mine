package Repository;

import Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Course_Repository extends JpaRepository<Course,Integer> {
    List<Course> findByCourseName(String CourseName);
    List<Course> findByCourse_ID(long Course_ID);
    List<Course> findByStartDateAndEndDate(String StartDate,String EndDate);
    List<Course> findByCourseType(String CourseType);
    List<Course> findByInstructorName(String InstructorName);
    List<Course> findByStatus(String Status);
    Course findByCourseIdandCourseName(long Course_ID, String CourseName);
}
