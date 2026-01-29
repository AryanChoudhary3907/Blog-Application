package com.Aryan.Blog.Application.repository;

import com.Aryan.Blog.Application.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post , Long> {

    List<Post> findByUserId(Long userId);

    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content, Pageable pageable
    );

    // Fix N+1 for pagination
    @EntityGraph(attributePaths = {"user"})
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user")
    Page<Post> findAllWithUser(Pageable pageable);


    @Query("""
    SELECT DISTINCT p FROM Post p
    LEFT JOIN FETCH p.user
    LEFT JOIN FETCH p.comment
    """)
    List<Post> findAllWithUserAndComments();

}
