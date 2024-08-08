package com.example.demo.dtos;

import com.example.demo.models.Comment;

public record CommentDto(Long id, String content, long likeCount) {
    public CommentDto(Comment comment) {
        this(comment.getId(), comment.getContent(), comment.getLikeCount());
    }
}
