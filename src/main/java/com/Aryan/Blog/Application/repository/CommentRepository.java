package com.Aryan.Blog.Application.repository;

import com.Aryan.Blog.Application.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByUserId(Long userId);


    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.post.id IN :postIds")
    List<Comment> findByPostIds(List<Long> postIds);


}
