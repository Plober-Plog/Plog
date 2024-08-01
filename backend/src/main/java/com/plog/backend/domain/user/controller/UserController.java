package com.plog.backend.domain.user.controller;

import com.plog.backend.domain.user.dto.RequestSignInDto;
import com.plog.backend.domain.user.dto.RequestSignUpDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.service.UserService;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.auth.JwtTokenProvider;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity registerUser(@Valid @RequestBody RequestSignUpDto request) {
        // 디버그 로그 추가
        UserController.log.info("Received Request: " + request.toString());

        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity modifyUser(@Valid @RequestBody RequestSignUpDto request) {
        // 디버그 로그 추가
        UserController.log.info("Received Request: " + request.toString());

        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity signIn(@Valid @RequestBody RequestSignInDto request) {
        try {
            log.info("Received Request: " + request.toString());
            String token = userService.login(request.getEmail(), request.getPassword());
            log.info("Received Token: " + token);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
