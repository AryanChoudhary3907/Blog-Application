package com.Aryan.Blog.Application.service;

import com.Aryan.Blog.Application.DTOs.AuthenticationDTOs.JWTResponse;
import com.Aryan.Blog.Application.DTOs.AuthenticationDTOs.LoginRequest;
import com.Aryan.Blog.Application.DTOs.AuthenticationDTOs.RegisterRequest;
import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDTO;
import com.Aryan.Blog.Application.Exception.CustomException.BadRequestException;
import com.Aryan.Blog.Application.entity.Role;
import com.Aryan.Blog.Application.entity.User;
import com.Aryan.Blog.Application.repository.UserRepository;
import com.Aryan.Blog.Application.utility.JwtAuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthUtil jwtAuthUtil;
    private final CustomUserDetailService customUserDetailService;


    public UserDTO register(RegisterRequest registerRequest) throws BadRequestException {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        // Convert DTO -> Entity
        User user = modelMapper.map(registerRequest, User.class);

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role
        user.setRole(Role.USER);

        // Save to DB
        userRepository.save(user);
        return modelMapper.map(user , UserDTO.class);
    }

    public JWTResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getUsername());

        String jwt = jwtAuthUtil.generateToken(userDetails);
        JWTResponse jwtResponse = new JWTResponse();
        jwtResponse.setUsername(request.getUsername());
        jwtResponse.setToken(jwt);
        jwtResponse.setRoles(userDetails.getAuthorities().toString());
        return jwtResponse;
    }

    public JWTResponse Refresh(String token) {
        String username = jwtAuthUtil.getUsername(token);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        if (!jwtAuthUtil.isTokenValid(token)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String jwt =jwtAuthUtil.generateRefreshToken(userDetails);
        JWTResponse jwtResponse = new JWTResponse();
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setToken(jwt);
        jwtResponse.setRoles(userDetails.getAuthorities().toString());
        return jwtResponse;
    }
}
