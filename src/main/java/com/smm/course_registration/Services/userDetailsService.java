package com.smm.course_registration.Services;

import com.smm.course_registration.Entity.User;
import com.smm.course_registration.Repository.userRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class userDetailsService implements UserDetailsService {
    private userRepository userRepository;

    public userDetailsService(userRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usero = userRepository.findByUsername(username);
        User user = usero.orElse(null);

        if (user == null) throw new UsernameNotFoundException("User not found");

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
