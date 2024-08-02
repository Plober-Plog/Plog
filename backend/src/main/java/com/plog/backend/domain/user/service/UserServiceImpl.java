package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.Entity;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(Entity requestSignUpDto) {
        //TODO [장현준]
        // - 회원 대표 이미지 추가
        User user = User.builder()
                .email(requestSignUpDto.getEmail())
                .searchId(requestSignUpDto.getSearchId())
                .password(passwordEncoder.encode(requestSignUpDto.getPassword()))
                .nickname(requestSignUpDto.getNickname())
                .gender(requestSignUpDto.getGender())
                .role(1)
                .state(1)
                .profileInfo("안녕하세용")
                .isAd(requestSignUpDto.isAd())
                .totalExp(0)
                .chatAuth(1)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User getUserBySearchId(String searchId) {
        Optional<User> user = userRepository.findUserBySearchId(searchId);
        return user.orElse(null);
    }
}
