package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.request.UserUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignUpRequestDto;
import com.plog.backend.domain.user.entity.*;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import com.plog.backend.global.auth.JwtTokenProvider;
import com.plog.backend.global.util.DateUtil;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;
    private final DateUtil dateUtil;

    @Override
    public User getUserBySearchId(String searchId) {
        log.info(">>> getUserBySearchId - 검색 ID: {}", searchId);
        return userRepository.findUserBySearchId(searchId)
                .orElseThrow(() -> {
                    log.error(">>> getUserBySearchId - 사용자 찾을 수 없음: {}", searchId);
                    return new IllegalArgumentException("User not found with searchId: " + searchId);
                });
    }

    @Override
    public String login(String email, String password) {
        log.info(">>> login - 이메일: {}, 패스워드: {}", email, password);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(">>> login - 이메일 또는 패스워드 잘못됨: {}", email);
                    return new IllegalArgumentException("Invalid email or password");
                });

        log.info(">>> login - 사용자 찾음: {}", user.toString());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), password)
        );

        log.info(">>> login - 인증된 사용자: {}", authentication.getPrincipal());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = "Bearer " + jwtTokenProvider.generateAccessToken(authentication);

        log.info(">>> login - 생성된 JWT 토큰: {}", jwtToken);

        return jwtToken;
    }

    @Override
    public User createUser(UserSignUpRequestDto userSignUpRequestDto) {
        log.info(">>> createUser - 사용자 회원가입 데이터: {}", userSignUpRequestDto);
        User user = User.builder()
                .email(userSignUpRequestDto.getEmail())
                .gender(Gender.gender(userSignUpRequestDto.getGender()))
                .role(Role.role(1))
                .state(State.state(1))
                .profileInfo("안녕하세용")
                .isAd(userSignUpRequestDto.isAd())
                .nickname(userSignUpRequestDto.getNickname())
                .totalExp(0)
                .chatAuth(ChatAuth.chatAuth(1))
                .searchId(userSignUpRequestDto.getSearchId())
                .password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
                .sidoCode(userSignUpRequestDto.getSidoCode())
                .gugunCode(userSignUpRequestDto.getGugunCode())
                .source(userSignUpRequestDto.getSource())
                .birthDate(userSignUpRequestDto.getBirthDate())
                //TODO [장현준] - image 추가
                .build();
        User savedUser = userRepository.save(user);
        log.info(">>> createUser - 사용자 생성됨: {}", savedUser);
        return savedUser;
    }

    @Override
    public Boolean checkUser(String searchId) {
        log.info(">>> checkUser - 검색 ID: {}", searchId);
        Optional<User> user = userRepository.findUserBySearchId(searchId);
        boolean isPresent = user.isPresent();

        log.info(">>> checkUser - 사용자 존재 여부: {}", isPresent);
        return isPresent;
    }

    @Override
    public Boolean checkEmail(String email) {
        log.info(">>> checkEmail - 이메일: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        boolean isPresent = user.isPresent();
        log.info(">>> checkEmail - 이메일 존재 여부: {}", isPresent);
        return isPresent;
    }

    @Transactional
    @Override
    public User updateUser(String token, UserUpdateRequestDto request) {
        log.info(">>> updateUser - 토큰: {}, 요청 데이터: {}", token, request);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> updateUser - 추출된 사용자 ID: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info(">>> updateUser - 사용자 찾음: {}", user);
            user.setNickname(request.getNickname());
            user.setProfileInfo(request.getProfile());
            user.setGender(Gender.gender(request.getGender()));
            user.setBirthDate(dateUtil.convertToLocalDate(request.getBirthDate()));
            user.setSource(request.getSource());
            user.setSidoCode(request.getSidoCode());
            user.setGugunCode(request.getGugunCode());

            User updatedUser = userRepository.save(user);
            log.info(">>> updateUser - 사용자 업데이트됨: {}", updatedUser);
            return updatedUser;
        } else {
            log.error(">>> updateUser - 사용자를 찾을 수 없음: {}", userId);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }
}

// TODO [장현준] Optional Exception 수정