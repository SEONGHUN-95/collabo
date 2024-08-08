package com.example.demo.application.post;

import com.example.demo.dtos.PostDto;
import com.example.demo.exceptions.PostNotFound;
import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPostService {
    private final PostRepository postRepository;
    public PostDto getPostDto(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);
        return new PostDto(post);
    }
}
