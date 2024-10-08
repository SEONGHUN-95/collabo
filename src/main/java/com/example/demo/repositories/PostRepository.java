package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_Username(String username);

    List<Post> findByTitleContaining(String keyword);
}
