package com.plog.backend.domain.user.controller;

import com.plog.backend.domain.user.dto.UserModifyDto;
import com.plog.backend.domain.user.dto.UserSignIngDto;
import com.plog.backend.domain.user.dto.UserSignUpDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // 회원 가입
    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserSignUpDto request) {
        log.info(">>> [POST] /user - 회원 가입 요청 데이터: {}", request);
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    // 회원 수정
    @PatchMapping
    public ResponseEntity<User> modifyUser(@RequestHeader("Authorization") String token, @Valid @RequestBody UserModifyDto request) {
        log.info(">>> [PATCH] /user - 회원 수정 요청 데이터: {}", request);

        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 문자열 제거
        }

        User user = userService.updateUser(token, request);
        return ResponseEntity.ok(user);
    }

    // 아이디 확인
    @GetMapping("/{searchId}")
    public ResponseEntity<Boolean> checkSearchId(@PathVariable("searchId") String searchId) {
        log.info(">>> [GET] /user/{} - 아이디 확인 요청", searchId);
        boolean result = userService.checkUser(searchId);
        return ResponseEntity.ok(result);
    }

    // 로그인 JWT 적용
    @PostMapping("/login")
    public ResponseEntity<String> signIn(@Valid @RequestBody UserSignIngDto request) {
        log.info(">>> [POST] /user/login - 로그인 요청 데이터: {}", request);
        try {
            String token = userService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error(">>> [POST] /user/login - 로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    // 이메일 중복 확인
    @PostMapping("/email")
    public ResponseEntity<Boolean> checkEmail(@Valid @RequestBody UserSignIngDto request) {
        log.info(">>> [POST] /user/email - 이메일 중복 확인 요청 데이터: {}", request);
        boolean result = userService.checkEmail(request.getEmail());
        return ResponseEntity.ok(result);
    }
}
