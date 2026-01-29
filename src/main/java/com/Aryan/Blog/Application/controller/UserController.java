package com.Aryan.Blog.Application.controller;

import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDTO;
import com.Aryan.Blog.Application.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Panel" , description = "Need to Login first")
public class UserController {

    private final UserService userService;

    @DeleteMapping()
    public ResponseEntity<String> deleteAccount(){

          UserDTO userDTO = userService.deleteAccount();
          return ResponseEntity.ok("Account Delete with username: "+userDTO.getUsername());
    }

}
