package com.example.demo.repositories;

import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.Like;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

}
