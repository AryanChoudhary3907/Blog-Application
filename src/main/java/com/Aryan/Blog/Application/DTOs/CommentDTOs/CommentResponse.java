package com.Aryan.Blog.Application.DTOs.CommentDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long commentId;
    private String username;
    private Long postId;
    private String text;
    private LocalDateTime createdAt;

}
