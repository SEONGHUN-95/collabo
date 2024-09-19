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


    public List<PostDto> getPostDtosByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("유효한 사용자 이름을 입력해야 합니다.");
        }
        List<Post> posts = postRepository.findByUser_Username(username);
        if (posts.isEmpty()) {
            throw new IllegalStateException("해당 사용자 이름으로 게시물을 찾을 수 없습니다.");
        }


        return posts.stream().map(PostDto::new).toList();
    }


    public List<PostDto> getPostDtosByKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("유효한 키워드를 입력해야 합니다.");
        }

        List<Post> posts = postRepository.findByTitleContaining(keyword);

        if (posts.isEmpty()) {
            throw new IllegalStateException("해당 키워드로 게시물을 찾을 수 없습니다.");
        }

        return posts.stream().map(PostDto::new).toList();
    }
}