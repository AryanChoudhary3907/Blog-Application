package com.Aryan.Blog.Application.controller;

import com.Aryan.Blog.Application.DTOs.AuthenticationDTOs.JWTResponse;
import com.Aryan.Blog.Application.DTOs.AuthenticationDTOs.LoginRequest;
import com.Aryan.Blog.Application.DTOs.AuthenticationDTOs.RegisterRequest;
import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDTO;
import com.Aryan.Blog.Application.entity.User;
import com.Aryan.Blog.Application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Login + SingUp Panel")
public class AuthController {

     private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "This Token is valid for 10 minutes")
    public JWTResponse createAuthenticationToken(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/Refresh")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "This Token is valid for 7 Days")
    public JWTResponse createRefreshToken(@RequestHeader("Authorization") String header) throws BadRequestException {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BadRequestException("Refresh token missing");
        }

        String refreshToken = header.substring(7);

      return authService.Refresh(refreshToken);
    }

    @PostMapping("/signup")
    public UserDTO register(@RequestBody RegisterRequest registerRequest) throws BadRequestException {
        return authService.register(registerRequest);
    }
}
