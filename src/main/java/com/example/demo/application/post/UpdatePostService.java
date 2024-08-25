package com.example.demo.application.post;

import com.example.demo.application.image.S3ImageService;
import com.example.demo.dtos.PostUpdateDto;
import com.example.demo.exceptions.ImageNotFoundException;
import com.example.demo.exceptions.PostNotFound;
import com.example.demo.models.Post;
import com.example.demo.models.PostImage;
import com.example.demo.repositories.PostImageRepository;
import com.example.demo.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdatePostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final S3ImageService s3ImageService;

    public void updatePost(Long userId, Long id, PostUpdateDto postUpdateDto) {

        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);
        // 권한 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the author of this post.");
        }

        // 제목 및 내용 수정
        if (postUpdateDto.getTitle() != null && postUpdateDto.getContent() != null) {
            post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
        }

        // 기존 이미지 삭제
        if (postUpdateDto.getDeleteImageIds() != null) {
            postUpdateDto.getDeleteImageIds().forEach(imageId -> {
                PostImage postImage = postImageRepository.findById(imageId)
                        .orElseThrow(ImageNotFoundException::new);
                s3ImageService.deleteImageFromS3(postImage.getImageUrl());
                postImageRepository.delete(postImage);
                post.removeImage(postImage);
            });
        }

        // 새로운 이미지 추가
        if (postUpdateDto.getNewImages() != null && !postUpdateDto.getNewImages().isEmpty()) {
            List<String> newImageUrls = postUpdateDto.getNewImages().stream()
                    .map(s3ImageService::upload)
                    .collect(Collectors.toList());
            newImageUrls.forEach(url -> {
                PostImage postImage = new PostImage(url, post);
                post.addImage(postImage.getImageUrl());
                postImageRepository.save(postImage);
            });
        }


        postRepository.save(post);
    }
}
