package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.request.UserPasswordCheckRequestDto;
import com.plog.backend.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignUpRequestDto;
import com.plog.backend.domain.user.dto.response.UserGetResponseDto;
import com.plog.backend.domain.user.entity.*;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import com.plog.backend.global.auth.JwtTokenProvider;
import com.plog.backend.global.exception.EntityNotFoundException;
import com.plog.backend.global.model.response.BaseResponseBody;
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
    public UserGetResponseDto getUser(String token) {
        log.info(">>> getUser - 토큰: {}", token);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> getUser - 추출된 사용자 ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error(">>> getUser - 사용자를 찾을 수 없음: {}", userId);
            return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        });

        log.info(">>> getUser - 사용자 정보: {}", user);

        return UserGetResponseDto.builder()
                .email(user.getEmail())
                .searchId(user.getSearchId())
                .nickname(user.getNickname())
                .gender(user.getGender().getValue())
                .birthDate(user.getBirthDate())
                .sidoCode(user.getSidoCode())
                .gugunCode(user.getGugunCode())
                .profileInfo(user.getProfileInfo())
                .isAd(user.isAd())
                .build();
    }

    @Override
    public String userSignIn(String email, String password) {
        log.info(">>> login - 이메일: {}, 패스워드: {}", email, password);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(">>> login - 이메일 또는 패스워드 잘못됨: {}", email);
                    return new IllegalArgumentException("Invalid email or password");
                });

        log.info(">>> login - 사용자 찾음: {}", user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
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
    public Boolean checkUserSearchId(String searchId) {
        log.info(">>> checkUser - 검색 ID: {}", searchId);
        Optional<User> user = userRepository.findUserBySearchId(searchId);
        boolean isPresent = user.isPresent();

        log.info(">>> checkUser - 사용자 존재 여부: {}", isPresent);
        return isPresent;
    }

    @Override
    public Boolean checkUserEmail(String email) {
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
            user.setBirthDate(request.getBirthDate());
            user.setSource(request.getSource());
            user.setSidoCode(request.getSidoCode());
            user.setGugunCode(request.getGugunCode());

            User updatedUser = userRepository.save(user);
            log.info(">>> updateUser - 사용자 업데이트됨: {}", updatedUser);
            return updatedUser;
        } else {
            log.error(">>> updateUser - 사용자를 찾을 수 없음: {}", userId);
            throw new EntityNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    @Transactional
    @Override
    public void deleteUser(String token) {
        log.info(">>> deleteUser - 토큰: {}", token);
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> deleteUser - 추출된 사용자 ID: {}", userId);

        // 사용자 조회
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(State.DELETED);
            userRepository.save(user);
            log.info(">>> deleteUser - 사용자 삭제 완료: {}", user);
        } else {
            log.error(">>> deleteUser - 사용자를 찾을 수 없음: {}", userId);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public BaseResponseBody checkPassword(String token, UserPasswordCheckRequestDto userPasswordCheckRequestDto) {
        log.info(">>> checkPassword - 토큰, RequestDto: {}, {}", token, userPasswordCheckRequestDto.toString());
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        log.info(">>> checkPassword - 추출된 사용자 ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        boolean result = passwordEncoder.matches(userPasswordCheckRequestDto.getPassword(), user.getPassword());

        log.info(">>> checkPassword - 비교: {}", result);
        if(result)
            return BaseResponseBody.of(200, "비밀번호가 확인 되었습니다.");
        else
            return BaseResponseBody.of(401, "비밀번호가 틀립니다.");
    }

    @Transactional
    @Override
    public void updatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
        log.info(">>> updatePassword - RequestDto: {}", userPasswordUpdateRequestDto);

        User user = userRepository.findById(userPasswordUpdateRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(userPasswordUpdateRequestDto.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        log.info(">>> updatePassword - 비밀번호 변경 성공");
    }
}
