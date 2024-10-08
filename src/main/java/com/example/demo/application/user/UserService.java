package com.example.demo.application.user;

import com.amazonaws.services.cloudformation.model.NameAlreadyExistsException;
import com.example.demo.application.image.S3ImageService;
import com.example.demo.dtos.PasswordUpdateDto;
import com.example.demo.dtos.UserDto;
import com.example.demo.exceptions.HandleGeneralException;
import com.example.demo.exceptions.InvalidPassword;
import com.example.demo.exceptions.PasswordMismatch;
import com.example.demo.exceptions.UserAlreadyExists;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.models.ProfileImage;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.UnexpectedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ImageService s3ImageService;

    public void createUser(String username, String email, String password) {
        try {
            String encodedPassword = passwordEncoder.encode(password);
            User user = new User(username, email, encodedPassword);
            String defaultImageUrl = "https://testbucketinthehouse.s3.ap-northeast-2.amazonaws.com/profile_image.jpeg";
            ProfileImage defaultProfileImage = new ProfileImage(defaultImageUrl);
            user.setProfileImage(defaultProfileImage);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExists("이미 존재하는 사용자입니다.", e);
        } catch (Exception e) {
            throw new HandleGeneralException("알 수 없는 오류가 발생했습니다.", e);
        }
    }

    public void updatePassword(String email, PasswordUpdateDto passwordUpdateDto) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFound("사용자를 찾지 못했습니다.: " + email));

        if (!passwordEncoder.matches(passwordUpdateDto.currentPassword(), user.getPassword())) {
            throw new InvalidPassword("비밀번호가 일치하지 않습니다.");
        }

        if (!passwordUpdateDto.newPassword().equals(passwordUpdateDto.confirmNewPassword())) {
            throw new PasswordMismatch("새로운 비밀번호가 서로 일치하지 않습니다.");
        }
        if (passwordEncoder.matches(passwordUpdateDto.confirmNewPassword(), user.getPassword())) {
            throw new PasswordMismatch("변경하고자 하는 비밀번호가 현재 비밀번호와 같습니다.");
        }
        String encodedNewPassword = passwordEncoder.encode(passwordUpdateDto.newPassword());
        user.updatePassword(encodedNewPassword);
        userRepository.save(user);
    }

    public void checkNameAvailability(String name) {
        if (userRepository.existsByUsername(name)) {
            throw new NameAlreadyExistsException("이미 사용 중인 이름입니다.");
        }
    }

    public void updateUserProfile(String currentUsername, String name, MultipartFile profileImage) {
        User user = userRepository.findUserByEmail(currentUsername)
                .orElseThrow(() -> new UserNotFound("사용자를 찾지 못했습니다."));

        if (name != null && !name.isEmpty()) {
            if (!name.equals(user.getUsername()) && userRepository.existsByUsername(name)) {
                throw new NameAlreadyExistsException("이름 '" + name + "'은 이미 있는 이름입니다.");
            }
            user.updateUsername(name);
        }

        if (profileImage != null && !profileImage.isEmpty()) {

            String imageUrl = s3ImageService.upload(profileImage);
            ProfileImage newProfileImage = new ProfileImage(imageUrl);
            user.setProfileImage(newProfileImage);
        }

        // 변경사항 저장
        userRepository.save(user);
    }

    public UserDto getUserProfile(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFound("사용자를 찾지 못했습니다."));
        return mapToUserDto(user);
    }

    public UserDto getUserProfile(Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFound("사용자를 찾지 못했습니다."));
        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(User user) {
        String profileImageUrl = (user.getProfileImage() != null) ? user.getProfileImage().getImageUrl() : null;

        List<String> followers = user.getFollowers().stream()
                .map(follow -> follow.getFollower().getUsername())
                .collect(Collectors.toList());

        List<String> following = user.getFollowing().stream()
                .map(follow -> follow.getFollowing().getUsername())
                .collect(Collectors.toList());

        return new UserDto(user.getEmail(), user.getUsername(), profileImageUrl, followers, following);
    }


}
