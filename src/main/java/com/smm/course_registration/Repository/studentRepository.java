package com.smm.course_registration.Repository;

import com.smm.course_registration.Entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface studentRepository extends JpaRepository<Student,Integer> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByNId(long nId);
    List<Student> findByName(String name);
    List<Student> findByLevel(String level);
    List<Student> findByNameAndNId(String name,long NId);

    @Override
    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
