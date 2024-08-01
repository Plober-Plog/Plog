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

import java.util.Optional;

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

        // 이메일로 사용자를 찾습니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // 토큰 인증객체
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        log.info("Authenticated user: {}", authentication.getPrincipal());

        // 인증 정보를 SecurityContextHolder에 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT를 생성하여 반환합니다.
        String jwtToken = "bearer " + jwtTokenProvider.generateAccessToken(authentication);

        log.info("Generated JWT Token: {}", jwtToken);

        return jwtToken;
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

    @Override
    public Boolean checkUser(String searchId) {
        Optional<User> user = userRepository.findUserBySearchId(searchId);

        // 회원이 있다면 true
        // 회원이 없다면 false
        return user.isPresent();
    }
}
