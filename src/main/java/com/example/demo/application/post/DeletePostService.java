package com.example.demo.application.post;

import com.example.demo.exceptions.PostNotFound;
import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePostService {
    private final PostRepository postRepository;

    public void deletePost(Long userId, Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the author of this post.");
        }

        postRepository.delete(post);
    }
}
