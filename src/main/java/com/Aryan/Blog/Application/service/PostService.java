package com.Aryan.Blog.Application.service;

import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import com.Aryan.Blog.Application.DTOs.PostDTOs.PostRequest;
import com.Aryan.Blog.Application.DTOs.PostDTOs.PostResponse;
import com.Aryan.Blog.Application.Exception.CustomException.BadRequestException;
import com.Aryan.Blog.Application.Exception.CustomException.ResourceNotFoundException;
import com.Aryan.Blog.Application.entity.Post;
import com.Aryan.Blog.Application.entity.Role;
import com.Aryan.Blog.Application.entity.User;
import com.Aryan.Blog.Application.repository.PostRepository;
import com.Aryan.Blog.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CommentService commentService;


    public PostResponse createPost(PostRequest request) {

        // Map request → Post
        Post post = modelMapper.map(request, Post.class);
        post.setCreatedAt(LocalDateTime.now());

        // Get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Set user to post
        post.setUser(user);

        // ADD post to user's post list (not replace)
        user.getPost().add(post);

        // Save post only (cascade handles relationship)
        Post savedPost = postRepository.save(post);
//        System.out.println("saved Post :"+ savedPost.getTitle());
        // Prepare response
        PostResponse map = modelMapper.map(savedPost, PostResponse.class);
        map.setUsername(username);
        return map;
    }


    public PostResponse updatePost(Long postId, PostRequest request) {

        // Get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        User user1 = post.getUser();
        if (!Objects.equals(user1.getId(), user.getId())) {
            throw new BadRequestException("You can't make change in this post!!!");
        }

        // Map request → Post
        if (request.getTitle() != null && !request.getTitle().isBlank() && !request.getTitle().isEmpty()) {
            post.setTitle(request.getTitle());
        }
        if (request.getContent() != null && !request.getContent().isBlank() && !request.getContent().isEmpty()) {
            post.setContent(request.getContent());
        }
        post.setUpdatedAt(LocalDateTime.now());

        // Save post only (cascade handles relationship)
        Post savedPost = postRepository.save(post);
        // Prepare response
        PostResponse map = modelMapper.map(savedPost, PostResponse.class);
        map.setUsername(username);
        return map;
    }

    public void deletePost(Long postId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // ADMIN can delete any post
        if (user.getRole() == Role.ADMIN) {
            postRepository.deleteById(postId);
            return;
        }

        // USER can delete only their own post
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete others' posts");
        }

        postRepository.deleteById(postId);
    }


    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        List<CommentResponse> commentResponse = commentService.getCommentByPost(postId);

        PostResponse response = modelMapper.map(post, PostResponse.class);
        response.setUsername(post.getUser().getUsername());
        return response;
    }

    public List<PostResponse> getAllPosts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // Fetch posts WITH user (avoid N+1)
        Page<Post> posts = postRepository.findAllWithUser(pageable);
        // Build final DTO list
        return posts.stream().map(post -> {
            PostResponse dto = modelMapper.map(post, PostResponse.class);
            dto.setUsername(post.getUser().getUsername());
            return dto;
        }).toList();
    }




    public List<PostResponse> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found by Id: "+userId));
        List<Post> posts = user.getPost();
        List<PostResponse> postResponses = posts.stream().map(post -> {
            PostResponse postResponse = modelMapper.map(post, PostResponse.class);
            postResponse.setUsername(user.getUsername());
            return postResponse;
        }).toList();

        return postResponses;
    }

    public List<PostResponse> searchPosts(String keyword, int page, int size) {
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Post> posts = postRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                        keyword, keyword, pageable);

        return posts.map(post -> {
            PostResponse dto = modelMapper.map(post, PostResponse.class);
            dto.setUsername(post.getUser().getUsername());
            return dto;
        }).toList();
    }
}
