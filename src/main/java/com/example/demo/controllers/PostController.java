package com.example.demo.controllers;

import com.example.demo.application.post.CreatePostService;
import com.example.demo.application.post.DeletePostService;
import com.example.demo.application.post.GetPostService;
import com.example.demo.application.post.GetPostsService;
import com.example.demo.application.post.LikePostService;
import com.example.demo.application.post.UpdatePostService;
import com.example.demo.dtos.PostCreateDto;
import com.example.demo.dtos.PostDto;
import com.example.demo.dtos.PostUpdateDto;
import com.example.demo.exceptions.AuthenticationException;
import com.example.demo.exceptions.PostNotFound;
import com.example.demo.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "게시글 API")
public class PostController {
    private final GetPostsService getPostsService;
    private final GetPostService getPostService;
    private final CreatePostService createPostService;
    private final UpdatePostService updatePostService;
    private final DeletePostService deletePostService;
    private final LikePostService likePostService;

    @GetMapping
    @Operation(summary = "전체 posts 받아오기")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getPosts() {
        List<PostDto> postDtoList = getPostsService.getPostDtos();
        return postDtoList;
    }

    @GetMapping("/{postId}")
    @Operation(summary = "특정 게시글 받아오기")
    public PostDto getPost(@PathVariable Long postId) {
        PostDto postDto = getPostService.getPostDto(postId);
        return postDto;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "게시글 쓰기")
    public void createPost(Authentication authentication,
                           @RequestPart("post") @Valid PostCreateDto postCreateDto,
                           @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Long userId = getUserIdFromAuthentication(authentication);
        createPostService.createPost(userId, postCreateDto, images);
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "게시글 수정", description = "postId로 수정할 게시글 지정")
    public void updatePost(Authentication authentication,
                           @PathVariable Long postId,
                           @RequestBody PostUpdateDto postUpdateDto
    ) {
        Long userId = getUserIdFromAuthentication(authentication);
        updatePostService.updatePost(userId, postId, postUpdateDto);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "게시글 삭제")
    public void deletePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        deletePostService.deletePost(userId, postId);
    }

    @PostMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "게시글 좋아요")
    public void likePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        likePostService.likePost(userId, postId);
    }

    @PostMapping("/{postId}/unlike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "게시글 좋아요 취소")
    public void unlikePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        likePostService.unlikePost(userId, postId);
    }

    @ExceptionHandler(PostNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String postNotFound() {
        return "게시물을 찾을 수 없습니다.";
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
}

