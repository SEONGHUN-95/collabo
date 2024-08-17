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
@RequestMapping("/posts")
@CrossOrigin
@RequiredArgsConstructor
public class PostController {
    private final GetPostsService getPostsService;
    private final GetPostService getPostService;
    private final CreatePostService createPostService;
    private final UpdatePostService updatePostService;
    private final DeletePostService deletePostService;
    private final LikePostService likePostService;

    @GetMapping
    public List<PostDto> getPosts() {
        List<PostDto> postDtoList = getPostsService.getPostDtos();
        return postDtoList;
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable Long postId) {
        PostDto postDto = getPostService.getPostDto(postId);
        return postDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(Authentication authentication, @RequestBody PostCreateDto postCreateDto) {
        Long userId = getUserIdFromAuthentication(authentication);
        createPostService.createPost(userId, postCreateDto);
    }

    @PatchMapping("/{postId}")
    public void updatePost(Authentication authentication,
                           @PathVariable Long postId,
                           @RequestBody PostUpdateDto postUpdateDto) {
        Long userId = getUserIdFromAuthentication(authentication);
        updatePostService.updatePost(userId, postId, postUpdateDto);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        deletePostService.deletePost(userId, postId);
    }

    @PostMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void likePost(Authentication authentication, @PathVariable Long postId) {
        Long userId = getUserIdFromAuthentication(authentication);
        likePostService.likePost(userId, postId);
    }

    @PostMapping("/{postId}/unlike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
