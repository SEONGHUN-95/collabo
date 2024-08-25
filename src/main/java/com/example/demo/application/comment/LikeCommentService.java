package com.example.demo.application.comment;

import com.example.demo.exceptions.CommentNotFound;
import com.example.demo.exceptions.PostNotFound;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class LikeCommentService {
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void likeComment(Long userId, Long commentId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        // commentId로 댓글을 찾고, 해당 댓글이 postId와 일치하는지 확인
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(CommentNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        // 이미 좋아요한 경우 중복 방지
        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new IllegalStateException("User already liked this comment");
        }

        CommentLike commentLike = new CommentLike(user, comment);
        commentLikeRepository.save(commentLike);

        comment.increaseLikeCount();
        commentRepository.save(comment);
    }

    @Transactional
    public void unlikeComment(Long userId, Long commentId, Long postId) {
        // postId로 게시물이 존재하는지 확인
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        // commentId로 댓글을 찾고, 해당 댓글이 postId와 일치하는지 확인
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(CommentNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        // commentId와 userId를 기반으로 CommentLike 엔티티 조회
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
                .orElseThrow(() -> new IllegalStateException("Like not found for this user and comment"));

        commentLikeRepository.delete(commentLike);

        comment.decreaseLikeCount();
        commentRepository.save(comment);
    }
}
