package com.Aryan.Blog.Application.controller;

import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentRequest;
import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import com.Aryan.Blog.Application.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comment Panel" , description = "Need to Login first")
public class CommentController {

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete Comment By Comment ID")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Deletion done!!");
    }


    @PutMapping("/{commentId}")
    @Operation(summary = "Update Comment ")
    public CommentResponse updateComment(@PathVariable Long commentId, @RequestBody CommentRequest request){
        return commentService.updateComment(commentId,request);
    }

    @PostMapping("/{postId}")
    @Operation(summary = "Create Comment")
    public CommentResponse addComment(@PathVariable Long postId, @RequestBody CommentRequest request){
        return commentService.addComment(postId,request);
    }
}
