package Repository;

import Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Student_Repository extends JpaRepository<Student,Integer> {

    List<Student> findAll();
    List<Student> findStudentByN_ID(Integer N_ID);
    List<Student> findStudentByName(String Name);
    List<Student> findStudentByCourseID(long CourseID);
}
