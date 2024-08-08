package com.example.demo.application.comment;

import com.example.demo.dtos.CommentUpdateDto;
import com.example.demo.exceptions.CommentNotFound;
import com.example.demo.models.Comment;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void updateComment(Long userId, Long id, Long postId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findByIdAndPostId(id, postId).orElseThrow(CommentNotFound::new);
        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the author of this comment.");
        }
        comment.update(commentUpdateDto.content());
        commentRepository.save(comment);
    }
}
