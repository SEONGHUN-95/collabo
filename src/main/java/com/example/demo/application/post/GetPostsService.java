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
        List<Post> posts = postRepository.findByUser_Username(username);
        return posts.stream().map(PostDto::new).toList();
    }

    public List<PostDto> getPostDtosByKeyword(String keyword) {
        List<Post> posts = postRepository.findByTitleContaining(keyword);
        return posts.stream().map(PostDto::new).toList();
    }

}