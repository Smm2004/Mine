package com.smm.course_registration.Repository;

import com.smm.course_registration.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface student_Repository extends JpaRepository<Student,Integer> {

    Student findByNID(Integer NID);
    List<Student> findByName(String Name);
    List<Student> findByLevel(String Level);
    List<Student> findByNameAndNID(String Name,Integer NID);

}
