package com.plog.backend.global.auth.Controller;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    UserServiceImpl userService;

    // OAuth2 로그인 성공 시 호출되는 엔드포인트
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<?> loginOrRegisterG(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profileImage = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");

        return userService.loginOrRegister(email, name, profileImage, providerId, 1);
    }

    // OAuth2 로그인 성공 시 호출되는 엔드포인트
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> loginOrRegisterK(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profileImage = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");

        return userService.loginOrRegister(email, name, profileImage, providerId, 2);
    }

    // OAuth2 로그인 성공 시 호출되는 엔드포인트
    @GetMapping("/login/oauth2/code/naver")
    public ResponseEntity<?> loginOrRegisterN(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profileImage = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub");

        return userService.loginOrRegister(email, name, profileImage, providerId, 3);
    }
}