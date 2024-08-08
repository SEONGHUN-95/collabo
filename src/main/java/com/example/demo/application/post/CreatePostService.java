package com.example.demo.application.post;

import com.example.demo.dtos.PostCreateDto;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(Long userId, PostCreateDto postCreateDto) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Post post = new Post(user,
                postCreateDto.title(), postCreateDto.content());
        postRepository.save(post);
    }
}
