package com.example.demo.dtos;

import com.example.demo.models.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long userId,
        String username,
        String content,
        LocalDateTime createdAt,
        long likeCount
) {
    public CommentDto(Comment comment) {
        this(comment.getId(), comment.getUser().getId(), comment.getUser().getUsername(), comment.getContent(), comment.getCreatedAt(), comment.getLikeCount());
    }
}
