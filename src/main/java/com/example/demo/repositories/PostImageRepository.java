package com.example.demo.repositories;

import com.example.demo.models.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    // 필요한 경우 커스텀 메서드를 추가할 수 있습니다.
    List<PostImage> findByPostId(Long postId);

    // 예를 들어, 특정 게시글에 속한 모든 이미지를 삭제하는 메서드
    void deleteByPostId(Long postId);
}
