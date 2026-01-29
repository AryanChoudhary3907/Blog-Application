package com.Aryan.Blog.Application.DTOs.AuthenticationDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class JWTResponse {
    private String token;
    private String username;
    private String roles;
}
