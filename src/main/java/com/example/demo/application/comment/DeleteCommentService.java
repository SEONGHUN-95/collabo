package com.example.demo.application.comment;

import com.example.demo.exceptions.CommentNotFound;
import com.example.demo.models.Comment;
import com.example.demo.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCommentService {
    private final CommentRepository commentRepository;


    public void deleteComment(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(CommentNotFound::new);

        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the author of this comment.");
        }

        commentRepository.delete(comment);
    }
}
