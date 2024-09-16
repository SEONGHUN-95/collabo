package com.example.demo.repositories;

import com.example.demo.models.Follow;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowing(User following);

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}

