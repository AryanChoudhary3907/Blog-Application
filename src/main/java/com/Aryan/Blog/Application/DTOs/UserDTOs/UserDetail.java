package com.Aryan.Blog.Application.DTOs.UserDTOs;

import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import com.Aryan.Blog.Application.DTOs.PostDTOs.PostResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDetail {
    private Long id;
    private String username;
    private String email;
    private List<PostResponse> posts;
    private List<CommentResponse> comments;
}
