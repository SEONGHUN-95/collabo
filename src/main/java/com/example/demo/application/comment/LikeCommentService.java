package com.example.demo.application.comment;

import com.example.demo.exceptions.CommentNotFound;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.LikeRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeCommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void likeComment(Long userId, Long id, Long postId){
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        CommentLike commentLike = new CommentLike(user, comment);
        commentLikeRepository.save(commentLike);

        comment.increaseLikeCount();
        commentRepository.save(comment);
    }

    @Transactional
    public void unlikeComment(Long userId, Long id, Long postId){
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user).orElseThrow();
        commentLikeRepository.delete(commentLike);

        comment.decreaseLikeCount();
        commentRepository.save(comment);
    }

}
