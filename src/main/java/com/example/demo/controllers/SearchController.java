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

    @GetMapping("/author")
    @Operation(summary = "사용자 이름으로 게시물 검색", description = "비어있으면 예외 발생합니다.")
    public List<PostDto> searchPostsByUsername(
            @RequestParam(required = false) String username) {

        return getPostsService.getPostDtosByUsername(username);
    }

    @GetMapping("/title")
    @Operation(summary = "제목으로 게시물 검색", description = "비어있으면 예외 발생합니다.")
    public List<PostDto> searchPostsByTitle(
            @RequestParam(required = false) String title) {
        return getPostsService.getPostDtosByKeyword(title);
    }

}

