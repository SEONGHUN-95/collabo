package com.example.demo.application.user;

import com.example.demo.dtos.UserDto;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.Follow;
import com.example.demo.models.User;
import com.example.demo.repositories.FollowRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void followUser(Long followerId, Long userId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + followerId));
        User following = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            Follow follow = new Follow(follower, following);
            followRepository.save(follow);
        }
    }


    @Transactional
    public void unfollowUser(Long followerId, Long followingId) throws UserNotFound {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new UserNotFound("팔로우 관계가 존재하지 않습니다."));

        followRepository.delete(follow);
    }

    public UserDto getUserWithFollowersAndFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        // 팔로워와 팔로잉을 각각 List<String>으로 변환
        List<String> followers = followRepository.findByFollowing(user).stream()
                .map(follow -> follow.getFollower().getUsername()) // 팔로워의 username
                .collect(Collectors.toList());

        List<String> following = followRepository.findByFollower(user).stream()
                .map(follow -> follow.getFollowing().getUsername()) // 팔로잉한 사용자의 username
                .collect(Collectors.toList());

        // UserDto 생성
        return new UserDto(
                user.getEmail(),
                user.getUsername(),
                user.getProfileImage().getImageUrl(),
                followers,
                following
        );
    }

}
