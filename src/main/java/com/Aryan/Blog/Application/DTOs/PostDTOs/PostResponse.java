package com.Aryan.Blog.Application.DTOs.PostDTOs;

import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostResponse {
    private Long postId;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
