package com.smm.course_registration.Repository;

import com.smm.course_registration.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
