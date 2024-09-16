package com.example.demo.application.post;

import com.example.demo.dtos.PostDto;
import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPostsService {
    private final PostRepository postRepository;

    public List<PostDto> getPostDtos() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostDto::new).toList();
    }

    public List<PostDto> searchPosts(String username, String keyword) {
        // 검색 조건이 모두 없으면 예외 처리
        if (username == null && keyword == null) {
            throw new IllegalArgumentException("검색 조건을 하나 이상 입력해야 합니다.");
        }

        // 사용자 이름으로 검색
        if (username != null) {
            return getPostDtosByUsername(username);
        }

        // 키워드로 검색
        return getPostDtosByKeyword(keyword);
    }

    public List<PostDto> getPostDtosByUsername(String username) {
        List<Post> posts = postRepository.findByUser_Username(username);
        return posts.stream().map(PostDto::new).toList();
    }

    public List<PostDto> getPostDtosByKeyword(String keyword) {
        List<Post> posts = postRepository.findByTitleContaining(keyword);
        return posts.stream().map(PostDto::new).toList();
    }

}