package com.plog.backend.domain.user.controller;

import com.plog.backend.domain.user.dto.Entity;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity registerUser(@Valid @RequestBody Entity request) {
        // 디버그 로그 추가
        UserController.log.info("Received Request: " + request.toString());

        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity modifyUser(@Valid @RequestBody Entity request) {
        // 디버그 로그 추가
        UserController.log.info("Received Request: " + request.toString());

        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }
}
