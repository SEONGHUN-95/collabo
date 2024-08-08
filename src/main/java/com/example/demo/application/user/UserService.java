package com.example.demo.application.user;

import com.example.demo.exceptions.UserAlreadyExists;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encodedPassword);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExists();
        }

    }
}
