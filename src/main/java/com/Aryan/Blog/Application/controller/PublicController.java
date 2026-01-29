package com.Aryan.Blog.Application.controller;

import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import com.Aryan.Blog.Application.DTOs.PostDTOs.PostResponse;
import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDTO;
import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDetail;
import com.Aryan.Blog.Application.service.CommentService;
import com.Aryan.Blog.Application.service.PostService;
import com.Aryan.Blog.Application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Tag(name = "Public Panel", description = "No need to Login")
public class PublicController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Search user By UserID")
    public UserDetail getById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/post/{postId})")
    @Operation(summary = "Search Post by PostID")
    public PostResponse getPostById(Long postId) {
        return postService.getPostById(postId);
    }

    @GetMapping("/post")
    @Operation(summary = "Show All Posts")
    public List<PostResponse> getALlPost(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPosts(page, size);
    }

    @GetMapping("/post/{userId}")
    @Operation(summary = "Show Post By UserId")
    public List<PostResponse> getPostsByUser(Long userId) {
        return postService.getPostsByUser(userId);
    }

    // Search Posts (Paginated)
    @GetMapping("/search")
    @Operation(summary = "Search Post by Keywords")
    public List<PostResponse> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.searchPosts(q, page, size);
    }

    @GetMapping("user/getAll")
    @Operation(summary = "Show All Users")
    public List<UserDTO> getALl() {
        return userService.getAll();
    }

    @GetMapping("comment/{postId}")
    @Operation(summary = "Show All Comments of a Post")
    public List<CommentResponse> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentByPost(postId);
    }
}
