package com.plog.backend.domain.user.controller;

import com.plog.backend.domain.user.dto.request.UserEmailCheckRequestDto;
import com.plog.backend.domain.user.dto.request.UserUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignInRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignUpRequestDto;
import com.plog.backend.domain.user.dto.response.UserResponseDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.exception.InvalidEmailFormatException;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.model.response.BaseResponseBody;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;
    private final JwtTokenUtil jwtTokenUtil;

    // 이메일 검증 패턴
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // 회원 가입
    @PostMapping
    public ResponseEntity<BaseResponseBody> createUser(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        log.info(">>> [POST] /user - 회원 가입 요청 데이터: {}", userSignUpRequestDto);
        if(userSignUpRequestDto.getEmail() == null || userSignUpRequestDto.getEmail().trim().isEmpty()) {
            throw new NotValidRequestException("email은 필수 필드입니다.");
        }
        if (userSignUpRequestDto.getEmail().matches(EMAIL_PATTERN)) {
            throw new InvalidEmailFormatException("Invalid email format");
        }
        if(userSignUpRequestDto.getPassword() == null || userSignUpRequestDto.getPassword().trim().isEmpty()) {
            throw new NotValidRequestException("password는 필수 필드입니다.");
        }
        if(userSignUpRequestDto.getSearchId() == null || userSignUpRequestDto.getSearchId().trim().isEmpty()) {
            throw new NotValidRequestException("검색 ID는 필수 입력 값입니다.");
        }
        if(userSignUpRequestDto.getNickname() == null || userSignUpRequestDto.getNickname().trim().isEmpty()) {
            throw new NotValidRequestException("닉네임은 필수 입력 값입니다.");
        }

        User user = userService.createUser(userSignUpRequestDto);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원가입이 완료되었습니다."));
    }

    // 회원 수정
    @PatchMapping
    public ResponseEntity<BaseResponseBody> updateUser(@RequestHeader("Authorization") String token, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        log.info(">>> [PATCH] /user - 회원 수정 요청 데이터: {}", userUpdateRequestDto);

        // userUpdateRequestDto 유효성 검사
        if(userUpdateRequestDto.getNickname() == null || userUpdateRequestDto.getNickname().trim().isEmpty()) {
            throw new NotValidRequestException("닉네임은 필수 입력 값입니다.");
        }

        if(userUpdateRequestDto.getSearchId() == null || userUpdateRequestDto.getSearchId().trim().isEmpty()) {
            throw new NotValidRequestException("검색 ID는 필수 입력 값입니다.");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 문자열 제거
        }

        User user = userService.updateUser(token, userUpdateRequestDto);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "회원 정보 수정이 완료되었습니다."));
    }

    // 중복 아이디 확인
    @GetMapping("/{searchId}")
    public ResponseEntity<BaseResponseBody> checkSearchId(@PathVariable("searchId") String searchId) {
        log.info(">>> [GET] /user/{} - 아이디 확인 요청", searchId);
        boolean result = userService.checkUserSearchId(searchId);

        if(result)
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 존재하는 ID 입니다."));
        else
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "없는 검색 ID 입니다."));
    }

    // 이메일 중복 확인
    @PostMapping("/email")
    public ResponseEntity<BaseResponseBody> checkEmail(@RequestBody UserEmailCheckRequestDto userEmailCheckRequestDto) {
        log.info(">>> [POST] /user/email - 이메일 중복 확인 요청 데이터: {}", userEmailCheckRequestDto);
        boolean result = userService.checkUserEmail(userEmailCheckRequestDto.getEmail());
        if(result)
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 존재하는 Email 입니다."));
        else
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "없는 검색 Email 입니다."));
    }

    // 로그인 JWT 적용
    @PostMapping("/login")
    public ResponseEntity<BaseResponseBody> signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        log.info(">>> [POST] /user/login - 로그인 요청 데이터: {}", userSignInRequestDto);
        try {
            String token = userService.userSignIn(userSignInRequestDto.getEmail(), userSignInRequestDto.getPassword());
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "로그인이 완료되었습니다."));
        } catch (Exception e) {
            log.error(">>> [POST] /user/login - 로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(401).body(BaseResponseBody.of(401, "아이디 혹은 비밀번호가 맞지 않습니다."));
        }
    }

    // 회원 정보 조회
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@RequestHeader("Authorization") String token) {
        log.info(">>> [GET] /user - 회원 정보 조회 토큰: {}", token);
        return ResponseEntity.status(200).body(userService.getUser(token));
    }
}

//TODO [장현준]
// 1. api문서 수정하고, 변수 맞춰놓기
// 2. JWT 토큰 expire, invalid 예외 적용하기
