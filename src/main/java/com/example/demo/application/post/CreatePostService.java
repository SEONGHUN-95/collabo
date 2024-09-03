package com.example.demo.application.post;

import com.example.demo.application.image.S3ImageService;
import com.example.demo.dtos.PostCreateDto;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatePostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3ImageService s3ImageService;

    public void createPost(Long userId, PostCreateDto postCreateDto, List<MultipartFile> images) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        List<String> imageUrls = images != null ?
                         images.stream()
                        .map(s3ImageService::upload)
                        .collect(Collectors.toList()) : new ArrayList<>();

        // 게시글 생성 및 저장
        Post post = new Post(user, postCreateDto.getTitle(), postCreateDto.getContent());

        imageUrls.forEach(post::addImage);

        postRepository.save(post);
    }
}
