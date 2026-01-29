package com.Aryan.Blog.Application.service;

import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentRequest;
import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import com.Aryan.Blog.Application.Exception.CustomException.ResourceNotFoundException;
import com.Aryan.Blog.Application.entity.Comment;
import com.Aryan.Blog.Application.entity.Post;
import com.Aryan.Blog.Application.entity.Role;
import com.Aryan.Blog.Application.entity.User;
import com.Aryan.Blog.Application.repository.CommentRepository;
import com.Aryan.Blog.Application.repository.PostRepository;
import com.Aryan.Blog.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    public CommentResponse addComment(Long postId, CommentRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = modelMapper.map(request, Comment.class);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        comment.setUser(user);

        // Add to post only (not required but keeps memory in sync)
        post.getComment().add(comment);

        // Save the comment only → cascade handles everything else
        Comment saved = commentRepository.save(comment);

        CommentResponse map = modelMapper.map(saved, CommentResponse.class);
        map.setUsername(username);
        map.setPostId(postId);
        return map;
    }



    public CommentResponse updateComment(Long commentId, CommentRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("comment not found by ID: "+commentId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // USER can update only their own post
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot Update others' comments");
        }

        if(request.getText() != null && !request.getText().isEmpty() && !request.getText().isBlank()){
            comment.setText(request.getText());
        }

        commentRepository.save(comment);
        CommentResponse map = modelMapper.map(comment, CommentResponse.class);
        map.setUsername(username);
        map.setPostId(comment.getPost().getId());
        return map;
    }

    public void deleteComment(Long commentId){

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("comment not found by ID: "+commentId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ADMIN can delete any comment
        if (user.getRole() == Role.ADMIN) {
            commentRepository.deleteById(commentId);
            return;
        }

        // USER can delete only their own comment
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete others' comments");
        }

        commentRepository.deleteById(commentId);
    }

    public List<CommentResponse> getCommentByPost(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Comment> comments = post.getComment();

        List<CommentResponse> commentResponses = post.getComment().stream()
                .map(c -> {
                    CommentResponse response = new CommentResponse();
                    response.setCommentId(c.getId());
                    response.setText(c.getText());
                    response.setUsername(c.getUser().getUsername());
                    response.setCreatedAt(c.getCreatedAt());
                    response.setPostId(postId);
                    return response;
                })
                .collect(Collectors.toList());

        return commentResponses;
    }

    public List<CommentResponse> getCommentsByUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user not found by id: " + userId));
        List<CommentResponse> commentResponses = user.getComment().stream()
                .map(c -> {
                    CommentResponse response = new CommentResponse();
                    response.setCommentId(c.getId());
                    response.setText(c.getText());
                    response.setUsername(c.getUser().getUsername());
                    response.setCreatedAt(c.getCreatedAt());
                    response.setPostId(c.getPost().getId());
                    return response;
                })
                .collect(Collectors.toList());

        return commentResponses;
    }

    public Map<Long, List<CommentResponse>> getCommentsGroupedByPostIds(List<Long> postIds) {

        List<Comment> comments = commentRepository.findByPostIds(postIds);

        // Grouping by PostId USING ENTITY
        return comments.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getPost().getId(),
                        Collectors.mapping(comment -> {
                            CommentResponse dto = modelMapper.map(comment, CommentResponse.class);
                            dto.setUsername(comment.getUser().getUsername());
                            return dto;
                        }, Collectors.toList())
                ));
    }


}
