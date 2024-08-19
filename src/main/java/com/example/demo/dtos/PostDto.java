package com.example.demo.dtos;

import com.example.demo.models.Post;

public record PostDto(
        String id,
        String username,
        String email,
        String title,
        String content,
        long likeCount
) {
    public PostDto(Post post) {
        this(post.getId().toString(), post.getUser().getUsername(), post.getUser().getEmail(), post.getTitle(), post.getContent(), post.getLikeCount());
    }
}
