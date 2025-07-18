package com.smm.course_registrationETET;
import com.smm.course_registration.CourseRegistrationApplication;
import com.smm.course_registration.Repository.courseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate; // For making HTTP requests
import org.springframework.boot.test.web.server.LocalServerPort; // To get the random port
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

import com.smm.course_registration.DTO.uLoginDTO;
import com.smm.course_registration.DTO.uRegisterDTO;
import com.smm.course_registration.Entity.Course;
import com.smm.course_registration.Repository.userRepository; // To verify user creation
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CourseRegistrationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityActions {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private userRepository userRepository; // To set up test users in the DB
    @Autowired
    private courseRepository courseRepository; // To set up test courses in the DB

    private String baseUrl() {
        return "http://localhost:" + port;
    }


    private String authenticate(String username, String password) {
        uLoginDTO loginRequest = new uLoginDTO(username, password); // Assumes uLoginDTO has public constructor
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
    void adminCanDefineCourse() {
        // Arrange: Register an Admin user
        uRegisterDTO adminRegister = new uRegisterDTO();
        adminRegister.setUsername("testadmin");
        adminRegister.setPassword("adminpass");
        adminRegister.setRole("ROLE_ADMIN");
        restTemplate.postForEntity(baseUrl() + "/auth/register", adminRegister, String.class);

        Course newCourse = new Course("DevOps 101", LocalDate.now(), LocalDate.now().plusMonths(2), "IT", "Mr. Admin");

        ResponseEntity<Course> response = authenticatedRestTemplate("testadmin", "adminpass")
                .postForEntity(baseUrl() + "/Courses/Define", newCourse, Course.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCourseName()).isEqualTo("DevOps 101");

        // Verify it's in the database
        assertThat(courseRepository.findByCourseName("DevOps 101")).isNotEmpty();
    }

    @Test
    void studentCannotDefineCourse() {
        // Arrange: Register a Student user
        uRegisterDTO studentRegister = new uRegisterDTO();
        studentRegister.setUsername("teststudent");
        studentRegister.setPassword("studentpass");
        studentRegister.setRole("ROLE_STUDENT");
        restTemplate.postForEntity(baseUrl() + "/auth/register", studentRegister, String.class);

        Course newCourse = new Course("Forbidden Course", LocalDate.now(), LocalDate.now().plusMonths(1), "Art", "Ms. Student");

        ResponseEntity<String> response = authenticatedRestTemplate("teststudent", "studentpass")
                .postForEntity(baseUrl() + "/Courses/Define", newCourse, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN); // Expect 403 Forbidden
        assertThat(courseRepository.findByCourseName("Forbidden Course")).isEmpty(); // Verify not in DB
    }

    @Test
    void adminCanSoftDeleteCourse() {
        uRegisterDTO adminRegister = new uRegisterDTO();
        adminRegister.setUsername("deleteadmin");
        adminRegister.setPassword("deletepass");
        adminRegister.setRole("ROLE_ADMIN");
        restTemplate.postForEntity(baseUrl() + "/auth/register", adminRegister, String.class);

        Course courseToDelete = new Course("Course To Delete", LocalDate.now(), LocalDate.now().plusMonths(1), "Math", "Dr. X");
        courseToDelete = courseRepository.save(courseToDelete); // Save directly to DB

        String url = baseUrl() + "/Courses/Soft-Delete?id=" + courseToDelete.getCourseId() + "&name=" + courseToDelete.getCourseName();
        ResponseEntity<Void> response = authenticatedRestTemplate("deleteadmin", "deletepass")
                .exchange(url, org.springframework.http.HttpMethod.PUT, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Course updatedCourse = courseRepository.findByCourseId(courseToDelete.getCourseId());
        assertThat(updatedCourse.getStatus()).isEqualTo("Deleted"); // Verify status updated in DB
    }

    @Test
    void studentCannotSoftDeleteCourse() {
        uRegisterDTO studentRegister = new uRegisterDTO();
        studentRegister.setUsername("deletestudent");
        studentRegister.setPassword("studentpass");
        studentRegister.setRole("ROLE_STUDENT");
        restTemplate.postForEntity(baseUrl() + "/auth/register", studentRegister, String.class);

        Course courseToProtect = new Course("Course To Protect", LocalDate.now(), LocalDate.now().plusMonths(1), "Science", "Dr. Y");
        courseToProtect = courseRepository.save(courseToProtect);

        String url = baseUrl() + "/Courses/Soft-Delete?id=" + courseToProtect.getCourseId() + "&name=" + courseToProtect.getCourseName();
        ResponseEntity<String> response = authenticatedRestTemplate("deletestudent", "studentpass")
                .exchange(url, org.springframework.http.HttpMethod.PUT, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Course unchangedCourse = courseRepository.findByCourseId(courseToProtect.getCourseId());
        assertThat(unchangedCourse.getStatus()).isNotEqualTo("Deleted");
    }

    @Test
    void userRegistrationSuccess() {
        uRegisterDTO registerRequest = new uRegisterDTO();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("newpass");
        registerRequest.setRole("ROLE_STUDENT");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<uRegisterDTO> request = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/auth/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("User registered successfully");
        assertThat(userRepository.findByUsername("newuser")).isNotNull();
    }

    @Test
    void userLoginSuccess() {
        uRegisterDTO registerRequest = new uRegisterDTO();
        registerRequest.setUsername("loginuser");
        registerRequest.setPassword("loginpass");
        registerRequest.setRole("ROLE_STUDENT");
        restTemplate.postForEntity(baseUrl() + "/auth/register", registerRequest, String.class);

        uLoginDTO loginRequest = new uLoginDTO("loginuser", "loginpass");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<uLoginDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/auth/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Login successful for user: loginuser");
    }

    @Test
    void userLoginFailure_BadCredentials() {
        uLoginDTO loginRequest = new uLoginDTO("nonexistent", "wrongpass");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<uLoginDTO> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/auth/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED); // Assuming 401 for bad credentials
        assertThat(response.getBody()).contains("Invalid username or password");
    }
}
