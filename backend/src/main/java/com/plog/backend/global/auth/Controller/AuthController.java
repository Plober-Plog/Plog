package com.plog.backend.global.auth.Controller;

import com.plog.backend.global.auth.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> tokenRequest, HttpServletRequest request) {
        String refreshToken = tokenRequest.get("refreshToken");
        if (jwtTokenProvider.validateRefreshToken(refreshToken, request)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            return ResponseEntity.ok().header("Authorization", "Bearer " + newAccessToken).body("토큰이 발급되었습니다.");
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("토큰이 만료되었습니다.");
        }
    }


}
