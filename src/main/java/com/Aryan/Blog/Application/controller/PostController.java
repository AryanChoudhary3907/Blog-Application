package com.Aryan.Blog.Application.controller;

import com.Aryan.Blog.Application.DTOs.PostDTOs.PostRequest;
import com.Aryan.Blog.Application.DTOs.PostDTOs.PostResponse;
import com.Aryan.Blog.Application.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post Panel" , description = "Need to Login first")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest){
        PostResponse post = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
    
    @PutMapping("/update/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest request){
        PostResponse postResponse = postService.updatePost(postId,request);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){

        postService.deletePost(postId);
        return ResponseEntity.ok("Deletion Done!!");
    }

}
