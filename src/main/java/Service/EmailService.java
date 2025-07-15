package Service;

import Entity.Course;
import Entity.Student;
import Repository.Course_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;
    Course_Repository course_repository;

    @Autowired
    public EmailService(JavaMailSender mailSender,  Course_Repository course_repository) {
        this.mailSender = mailSender;
        this.course_repository = course_repository;
    };

    public void SendEmail(Long id, String name, Student student) {
        SimpleMailMessage message = new SimpleMailMessage();

        Course course = course_repository.findByCourseIdandCourseName(id, name);
        String body = "Congratulations " + student.GetName() + "! You have succesfully registered in " + name;
        message.setFrom("marghlani.siraj@gmail.com");
        message.setTo(student.GetEmail());
        message.setCc(course.GetInstructorName() + "@gmail.com");
        message.setSubject("Course Registration");
        message.setText(body);

        mailSender.send(message);
    }
}
