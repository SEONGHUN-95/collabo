package com.example.demo.controllers;

import com.example.demo.application.post.GetPostsService;
import com.example.demo.dtos.PostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "검색 API")
public class SearchController {
    private final GetPostsService getPostsService;

    // 검색 조건에 따른 게시물 검색 (사용자 이름 또는 키워드)
    @GetMapping
    @Operation(summary = "게시물 검색", description = "사용자 이름 또는 키워드로 게시물 검색")
    public List<PostDto> searchPosts(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String keyword) {

        return getPostsService.searchPosts(username, keyword);
    }
}

