package com.example.demo.application.user;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetUserService {
    private final UserRepository userRepository;

    public GetUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}