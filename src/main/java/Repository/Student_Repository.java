package Repository;

import Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Student_Repository extends JpaRepository<Student,Integer> {

    Student findByN_ID(Integer N_ID);
    List<Student> findByName(String Name);
    List<Student> findByLevel(String Level);
    List<Student> findByNameAndN_ID(String Name,Integer N_ID);

}
