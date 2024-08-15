package com.plog.backend.global.auth.Controller;

import com.plog.backend.global.auth.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> tokenRequest, HttpServletRequest request) {
        String refreshToken = tokenRequest.get("refreshToken");
        if (jwtTokenProvider.validateRefreshToken(refreshToken, request)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            log.info("refresh token - 새로운 토큰 발급 : {}", newAccessToken);
            newAccessToken = "Bearer " + newAccessToken;
            return ResponseEntity.ok().header("Authorization", newAccessToken).body("토큰이 발급되었습니다. : "+ newAccessToken);
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("refresh 토큰이 만료되었습니다.");
        }
    }
}
