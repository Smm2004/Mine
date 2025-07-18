package com.smm.course_registrationETET;

import com.smm.course_registration.CourseRegistrationApplication;
import com.smm.course_registration.DTO.CourseRegistrationDTO;
import com.smm.course_registration.DTO.uLoginDTO;
import com.smm.course_registration.DTO.uRegisterDTO;
import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Entity.Student;
import com.smm.course_registration.Entity.User;
import com.smm.course_registration.Repository.courseRepository;
import com.smm.course_registration.Repository.studentRepository;
import com.smm.course_registration.Repository.userRepository;
import com.smm.course_registration.Services.courseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = CourseRegistrationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("Test")
public class StudentActions {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    courseRepository courseRepository;
    @Autowired
    studentRepository studentRepository;
    @Autowired
    courseService courseService;
    @Autowired
    userRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private String authenticate(String username, String password) {
        uLoginDTO loginRequest = new uLoginDTO(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<uLoginDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/auth/login", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        return "authenticated";
    }

    private TestRestTemplate authenticatedRestTemplate(String username, String password) {
        return restTemplate.withBasicAuth(username, password);
    }

    @Test
    void studentCanRegisterForCourse() {
        uRegisterDTO studentu = new uRegisterDTO();
        studentu.setUsername("teststudent");
        studentu.setPassword("studentpass");
        studentu.setRole("ROLE_STUDENT");
        ResponseEntity<String> studentRegisterResponse = restTemplate.postForEntity(baseUrl() + "/auth/register", studentu, String.class);
        assertThat(studentRegisterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Course courseToRegister = new Course("DevOps 101", LocalDate.now(), LocalDate.now().plusMonths(2), "IT", "Mr.Admin");
        Course savedCourse = courseRepository.save(courseToRegister);

        Student studentEntity = new Student();
        studentEntity.setName("Siraj Aldeen Marghalani");
        studentEntity.setLevel("2nd Year");
        studentEntity.setNID(1298397453L);
        studentEntity.setEmail("marghalani.siraj@gmail.com");
        Student savedStudentEntity = studentRepository.save(studentEntity);

        CourseRegistrationDTO dto = new CourseRegistrationDTO();
        dto.setId(savedCourse.getCourseId());
        dto.setName(savedCourse.getCourseName());
        dto.setNId(savedStudentEntity.getNID());

        ResponseEntity<Void> response = authenticatedRestTemplate("teststudent", "studentpass")
                .postForEntity(baseUrl() + "/Courses/Register", dto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Course updatedCourse = courseRepository.findByCourseIdAndCourseName(savedCourse.getCourseId(), savedCourse.getCourseName());
        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getStudents()).isNotEmpty();
        assertThat(updatedCourse.getStudents().stream().anyMatch(s -> s.getNID() == savedStudentEntity.getNID())).isTrue();

        Student updatedStudent = studentRepository.findByNId(savedStudentEntity.getNID()).orElseThrow();
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getCourses()).isNotEmpty();
        assertThat(updatedStudent.getCourses().stream().anyMatch(c -> c.getCourseId() == savedCourse.getCourseId())).isTrue();
    }
    @Test
    void studentCannotRegisterForMoreThanThreeCourses() {
        // Arrange: User setup (for authentication if making API calls, though here we call service directly)
        User userEntity = new User();
        userEntity.setUsername("overloadstudent");
        userEntity.setPassword(passwordEncoder.encode("pass123"));
        userEntity.setRole("STUDENT");
        userRepository.save(userEntity);

        // Arrange: Student setup
        Student student = new Student();
        student.setName("Max Courses");
        student.setLevel("3rd Year");
        student.setNID(987654321L);
        student.setEmail("max.courses@example.com");
        Student savedStudent = studentRepository.save(student); // Save student once

        Course course1 = createAndSaveCourse("Course A", "Instr 1");
        Course course2 = createAndSaveCourse("Course B", "Instr 2");
        Course course3 = createAndSaveCourse("Course C", "Instr 3");

        courseService.registerForCourse(course1.getCourseId(), course1.getCourseName(), savedStudent.getNID());
        courseService.registerForCourse(course2.getCourseId(), course2.getCourseName(), savedStudent.getNID());
        courseService.registerForCourse(course3.getCourseId(), course3.getCourseName(), savedStudent.getNID());

        Student studentWithCourses = studentRepository.findByNId(savedStudent.getNID())
                .orElseThrow(() -> new RuntimeException("Student not found for test"));
        assertThat(studentWithCourses.getCourseCount()).isEqualTo(3); // Verify student has 3 courses

        Course course4 = createAndSaveCourse("Course D", "Instr 4");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            courseService.registerForCourse(course4.getCourseId(), course4.getCourseName(), studentWithCourses.getNID());
        });

        assertThat(thrown.getMessage()).contains("cannot register for more than 3 courses.");

        Student finalStudentState = studentRepository.findByNId(savedStudent.getNID())
                .orElseThrow(() -> new RuntimeException("Student not found for test"));

        assertThat(finalStudentState.getCourseCount()).isEqualTo(3);
        assertThat(finalStudentState.getCourses()).doesNotContain(course4);
    }

    private Course createAndSaveCourse(String name, String instructor) {
        Course course = new Course();
        course.setCourseName(name);
        course.setInstructorName(instructor);
        course.setCourseType("Online");
        course.setStartDate(LocalDate.now().minusMonths(2));
        course.setEndDate(LocalDate.now().plusMonths(2));
        course.setStatus("Open");
        return courseRepository.save(course);
    }
}
