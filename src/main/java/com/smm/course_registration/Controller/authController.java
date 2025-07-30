package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.StudentRegistrationDTO;
import com.smm.course_registration.DTO.uLoginDTO;
import com.smm.course_registration.DTO.uRegisterDTO;
import com.smm.course_registration.Entity.User;
import com.smm.course_registration.Repository.userRepository;
import com.smm.course_registration.Services.studentService;
import com.smm.course_registration.Services.userDetailsService;
import com.smm.course_registration.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/auth")
public class authController {

    userRepository userRepository;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final userDetailsService userDetailsService;
    private final studentService studentservice;

    public authController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, userRepository userRepository, JwtUtil jwtUtil, userDetailsService userDetailsService,  studentService studentservice) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.studentservice = studentservice;
    }

    @Operation(summary = "Register page for admins")
    @PostMapping("/register/admin")
    public ResponseEntity<String> register(@RequestBody uRegisterDTO request) {
        if (userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully as " + request.getRole());
    }

    @Operation(summary = "Register page for students")
    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@ModelAttribute StudentRegistrationDTO registrationDTO, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            studentservice.registerStudent(registrationDTO, file);
            return new ResponseEntity<>("Student registered successfully!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred during registration.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Logging in using registered username and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody uLoginDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    static class AuthenticationResponse {
        private final String jwt;
        public AuthenticationResponse(String jwt) {
            this.jwt = jwt;
        }
        public String getJwt() {
            return jwt;
        }
    }
}