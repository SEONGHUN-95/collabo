package com.example.demo.controllers;

import com.example.demo.application.comment.CreateCommentService;
import com.example.demo.application.comment.DeleteCommentService;
import com.example.demo.application.comment.GetCommentsService;
import com.example.demo.application.comment.LikeCommentService;
import com.example.demo.application.comment.UpdateCommentService;
import com.example.demo.dtos.CommentCreateDto;
import com.example.demo.dtos.CommentDto;
import com.example.demo.dtos.CommentUpdateDto;
import com.example.demo.exceptions.AuthenticationException;
import com.example.demo.exceptions.CommentNotFound;
import com.example.demo.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "댓글 API")
public class CommentController {
    private final GetCommentsService getCommentsService;
    private final CreateCommentService createCommentService;
    private final UpdateCommentService updateCommentService;
    private final DeleteCommentService deleteCommentService;
    private final LikeCommentService likeCommentService;

    @GetMapping
    @Operation(summary = "전체 comments 받아오기", description = "jwt의 name과 commentDto의 email을 대조하여 수정/삭제 버튼 구현 필요")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        List<CommentDto> commentDtos =
                getCommentsService.getCommentDtos(postId);

        return commentDtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "댓글 쓰기")
    public void create(@PathVariable Long postId,
                       @RequestBody CommentCreateDto commentCreateDto,
                       Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        createCommentService.createComment(userId, postId, commentCreateDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 수정", description = "commentId, postId로 수정할 댓글 지정")
    public void update(
            @PathVariable Long commentId,
            @PathVariable Long postId,
            @RequestBody CommentUpdateDto commentUpdateDto,
            Authentication authentication
    ) {
        Long userId = getUserIdFromAuthentication(authentication);
        updateCommentService.updateComment(userId, commentId, postId, commentUpdateDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 삭제", description = "commentId, postId로 삭제할 댓글 지정")
    public void delete(@PathVariable Long commentId,
                       @PathVariable Long postId,
                       Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        deleteCommentService.deleteComment(userId, postId, commentId);
    }

    @PostMapping("/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "댓글 좋아요")
    public void likeComment(Authentication authentication, @PathVariable Long commentId, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        likeCommentService.likeComment(userId, commentId, postId);
    }

    @PostMapping("/{commentId}/unlike")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "댓글 좋아요 취소")
    public void unlikePost(Authentication authentication, @PathVariable Long commentId, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        likeCommentService.unlikeComment(userId, commentId, postId);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new AuthenticationException("Principal is not of type CustomUserDetails");
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Long userId = userDetails.getId();
        if (userId == null) {
            throw new AuthenticationException("User ID is null");
        }

        return userId;
    }

    @ExceptionHandler(CommentNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentNotFound() {
        return "댓글을 찾을 수 없습니다.";
    }


}
