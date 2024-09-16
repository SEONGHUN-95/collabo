package com.example.demo.application.post;

import com.example.demo.dtos.PostUpdateDto;
import com.example.demo.exceptions.PostNotFound;
import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePostService {
    private final PostRepository postRepository;

    public void updatePost(Long userId, Long id, PostUpdateDto postUpdateDto) {

        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);
        // 권한 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the author of this post.");
        }

        // 제목 및 내용 수정
        if (postUpdateDto.getTitle() != null && postUpdateDto.getContent() != null) {
            post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
        }

        postRepository.save(post);
    }


}
