package com.smm.course_registration.Controller;

import com.smm.course_registration.DTO.uLoginDTO;
import com.smm.course_registration.DTO.uRegisterDTO;
import com.smm.course_registration.Entity.User;
import com.smm.course_registration.Repository.userRepository;
import com.smm.course_registration.Services.userDetailsService;
import com.smm.course_registration.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class authController {

    userRepository userRepository;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final userDetailsService userDetailsService;

    public authController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, userRepository userRepository, JwtUtil jwtUtil, userDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody uRegisterDTO request) {
        if (userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists.");
        }

        if (!request.getRole().equals("ROLE_ADMIN") && !request.getRole().equals("ROLE_STUDENT")) {
            return ResponseEntity.badRequest().body("Role must be Admin or Student.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully as " + request.getRole());
    }

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