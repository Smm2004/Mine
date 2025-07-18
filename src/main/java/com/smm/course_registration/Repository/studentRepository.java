package com.smm.course_registration.Repository;

import com.smm.course_registration.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface studentRepository extends JpaRepository<Student,Integer> {

    Optional<Student> findByNId(long nId);
    List<Student> findByName(String name);
    List<Student> findByLevel(String level);
    List<Student> findByNameAndNId(String name,Integer NId);

}
