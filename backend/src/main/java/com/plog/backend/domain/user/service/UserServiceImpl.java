package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.RequestSignUpDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RequestSignUpDto requestSignUpDto) {
        User user = User.builder()
                .email(requestSignUpDto.getEmail())
                .gender(requestSignUpDto.getGender())
                .role(1)
                .state(1)
                .profileInfo("안녕하세용")
                .isAd(requestSignUpDto.isAd())
                .totalExp(0)
                .chatAuth(1)
                .searchId(requestSignUpDto.getSearchId())
                .password(passwordEncoder.encode(requestSignUpDto.getPassword()))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User getUserBySearchId(String searchId) {

        return null;
    }
}
