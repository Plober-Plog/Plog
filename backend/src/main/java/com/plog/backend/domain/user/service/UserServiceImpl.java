package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.RequestSignUpDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User getUserBySearchId(String searchId) {
        return userRepository.findUserBySearchId(searchId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with searchId: " + searchId));
    }

    @Override
    public String login(String email, String password) {
        log.info("login1 : {}, {}", email, password);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        log.info("login2 : {}, {}", email, password);
        log.info("Authenticated user: {}", authentication.getPrincipal());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // 인증 정보 SecurityContextHolder에 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateAccessToken(authentication);
    }

    @Override
    public User createUser(RequestSignUpDto requestSignUpDto) {
        User user = User.builder()
                .email(requestSignUpDto.getEmail())
                .gender(requestSignUpDto.getGender())
                .role(1)
                .state(1)
                .profileInfo("안녕하세용")
                .isAd(requestSignUpDto.isAd())
                .nickname(requestSignUpDto.getNickname())
                .totalExp(0)
                .chatAuth(1)
                .searchId(requestSignUpDto.getSearchId())
                .password(passwordEncoder.encode(requestSignUpDto.getPassword()))
                .build();
        return userRepository.save(user);
    }
}
