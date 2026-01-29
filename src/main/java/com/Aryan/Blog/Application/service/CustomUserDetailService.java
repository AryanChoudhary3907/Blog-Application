package com.Aryan.Blog.Application.service;

import com.Aryan.Blog.Application.entity.User;
import com.Aryan.Blog.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Map your User entity to Spring Security's UserDetails object
        return   org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // pass Email because email is unique
                .password(user.getPassword()) // Hashed password
                .roles(String.valueOf(user.getRole()))       // e.g., "USER" "ADMIN"
                .build();
    }
}
