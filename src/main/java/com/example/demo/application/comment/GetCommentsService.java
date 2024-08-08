package com.example.demo.application.comment;

import com.example.demo.dtos.CommentDto;
import com.example.demo.models.Comment;
import com.example.demo.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCommentsService {
    private final CommentRepository commentRepository;

    public List<CommentDto> getCommentDtos(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(CommentDto::new).toList();

    }
}
