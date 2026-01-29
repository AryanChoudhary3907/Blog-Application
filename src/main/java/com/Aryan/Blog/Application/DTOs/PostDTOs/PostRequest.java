package com.Aryan.Blog.Application.DTOs.PostDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String content;
}
