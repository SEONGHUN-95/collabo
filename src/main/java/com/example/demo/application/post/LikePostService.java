package com.example.demo.application.post;

import com.example.demo.exceptions.PostNotFound;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.LikeRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void likePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Like like = new Like(user, post);
        likeRepository.save(like);

        post.increaseLikeCount();
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        likeRepository.delete(like);

        post.decreaseLikeCount();
        postRepository.save(post);
    }
}
