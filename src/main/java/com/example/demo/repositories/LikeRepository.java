package com.example.demo.repositories;

import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);

//    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
//    long countByPost(@Param("post") Post post);

}
