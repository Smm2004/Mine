package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Repository.course_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailService {

    private final JavaMailSender mailSender;
    course_Repository course_repository;

    @Autowired
    public emailService(JavaMailSender mailSender, course_Repository course_repository) {
        this.mailSender = mailSender;
        this.course_repository = course_repository;
    };

    public void SendEmail(Long id, String name, Student student) {
        SimpleMailMessage message = new SimpleMailMessage();

        Course course = course_repository.findByCourseIdAndCourseName(id, name);
        String body = "Congratulations " + student.getName() + "! You have succesfully registered in " + name;
        message.setFrom("marghlani.siraj@gmail.com");
        message.setTo(student.getEmail());
        message.setCc(course.getInstructorName() + "@gmail.com");
        message.setSubject("Course Registration");
        message.setText(body);

        mailSender.send(message);
    }
}
