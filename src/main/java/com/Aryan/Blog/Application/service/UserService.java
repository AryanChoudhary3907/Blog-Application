package com.Aryan.Blog.Application.service;


import com.Aryan.Blog.Application.DTOs.CommentDTOs.CommentResponse;
import com.Aryan.Blog.Application.DTOs.PostDTOs.PostResponse;
import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDTO;
import com.Aryan.Blog.Application.DTOs.UserDTOs.UserDetail;
import com.Aryan.Blog.Application.Exception.CustomException.ResourceNotFoundException;
import com.Aryan.Blog.Application.entity.User;
import com.Aryan.Blog.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PostService postService;
    private final CommentService commentService;

    public UserDetail getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user not found by id: " + id));

        UserDetail map = modelMapper.map(user, UserDetail.class);
        List<PostResponse> postResponse = postService.getPostsByUser(user.getId());
        List<CommentResponse> commentResponse = commentService.getCommentsByUser(user.getId());
        map.setComments(commentResponse);
        map.setPosts(postResponse);
        return map;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserDTO dto = modelMapper.map(user, UserDTO.class);
            return dto;
        }).collect(Collectors.toList());
    }

    public UserDTO deleteAccount(){

        // Step 1: Get logged-in user's email
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Step 2: Fetch user from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Step 3: Delete user
        userRepository.delete(user);

        // Step 4: Convert to DTO (optional)

        return modelMapper.map(user, UserDTO.class);
    }
}
