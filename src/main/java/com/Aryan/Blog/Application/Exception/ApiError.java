package com.Aryan.Blog.Application.Exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {

    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ApiError(String message, String path) {
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
