package com.example.demo.application.user;

import com.example.demo.exceptions.SelfFollowException;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Follow;
import com.example.demo.models.User;
import com.example.demo.repositories.FollowRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void followUser(Long followerId, Long userId) {
        if (followerId.equals(userId)) {
            throw new SelfFollowException("나 자신을 팔로우할 수 없습니다.");
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFound("사용자가 존재하지 않습니다."));
        User following = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("해당 팔로워가 존재하지 않습니다."));

        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            Follow follow = new Follow(follower, following);
            followRepository.save(follow);
        }
    }


    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new UserNotFound("팔로우 관계가 존재하지 않습니다."));

        followRepository.delete(follow);
    }


}
