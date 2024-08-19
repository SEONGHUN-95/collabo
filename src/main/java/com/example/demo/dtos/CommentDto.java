package com.example.demo.dtos;

import com.example.demo.models.Comment;

public record CommentDto(
        Long id,
        String username,
        String email,
        String content,
        long likeCount
) {
    public CommentDto(Comment comment) {
        this(comment.getId(), comment.getUser().getUsername(), comment.getUser().getEmail(), comment.getContent(), comment.getLikeCount());
    }
}
