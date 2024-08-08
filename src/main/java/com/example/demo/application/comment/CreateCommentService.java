package com.example.demo.application.comment;

import com.example.demo.dtos.CommentCreateDto;
import com.example.demo.exceptions.PostNotFound;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCommentService {
    //TODO: 로그인 구현 후 회원정보 받아오기
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment(Long userId, Long postId, CommentCreateDto commentCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
        Comment comment = new Comment(post, user, commentCreateDto.content());
        commentRepository.save(comment);
    }
}
