package com.example.demo.application.user;

import com.example.demo.dtos.CustomUserInfoDto;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));
        CustomUserInfoDto dto = modelMapper.map(user, CustomUserInfoDto.class);
        return new CustomUserDetails(dto);
    }
}
