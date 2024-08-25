package com.example.demo.dtos;

import com.example.demo.models.Post;

import java.util.List;
import java.util.stream.Collectors;

public record PostDto(
        String id,
        String username,
        String email,
        String title,
        String content,
        long likeCount,
        List<String> imageUrls // 이미지 URL 목록 추가
) {
    public PostDto(Post post) {
        this(
                post.getId().toString(),
                post.getUser().getUsername(),
                post.getUser().getEmail(),
                post.getTitle(),
                post.getContent(),
                post.getLikeCount(),
                post.getImages().stream()
                        .map(image -> image.getImageUrl()) // PostImage 객체에서 URL을 추출하여 리스트로 변환
                        .collect(Collectors.toList())
        );
    }
}
