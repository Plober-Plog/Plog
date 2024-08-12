package com.plog.backend.global.auth.Controller;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OAuth2Controller {

    private final UserServiceImpl userService;

    // 생성자를 통해 UserServiceImpl을 주입받습니다.
    public OAuth2Controller(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login/oauth2")
    public String login(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            // 사용자 정보를 모델에 추가
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));

            // 성공 후 메인 페이지로 리다이렉션
            return "redirect:/main";
        } else {
            // 인증 실패 시 로그인 페이지로 리다이렉션
            return "redirect:/user/login?error";
        }
    }

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
