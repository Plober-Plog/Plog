package com.plog.backend.global.auth.Controller;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.auth.entity.OAuth2UserInfo;
import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/login/oauth2/code")
public class OAuth2Controller {

    private final UserServiceImpl userService;

    public OAuth2Controller(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/google")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam("code") String code) {
        return handleOAuth2Callback(code, 1);
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> handleKakaoCallback(@RequestParam("code") String code) {
        return handleOAuth2Callback(code, 2);
    }

    @GetMapping("/naver")
    public ResponseEntity<?> handleNaverCallback(@RequestParam("code") String code) {
        return handleOAuth2Callback(code, 3);
    }

    private ResponseEntity<?> handleOAuth2Callback(String code, int provider) {
        String accessToken = getAccessTokenFromProvider(code, provider);
        OAuth2UserInfo userInfo = getUserInfoFromProvider(accessToken, provider);
        return userService.loginOrRegister(
                userInfo.getEmail(),
                userInfo.getName(),
                userInfo.getProfileImage(),
                userInfo.getProviderId(),
                provider);
    }

    private String getAccessTokenFromProvider(String code, int provider) {
        // provider에 따라 구글, 카카오, 네이버에 맞게 토큰 요청을 보냄
        // 각 플랫폼의 토큰 엔드포인트로 POST 요청을 보내어 액세스 토큰을 받아야 함
        String tokenEndpoint = "";
        String clientId = "";
        String clientSecret = "";
        String redirectUri = "http://localhost:8080/user/login/oauth2/code/";

        if (provider == 1) { // 구글
            tokenEndpoint = "https://oauth2.googleapis.com/token";
            clientId = "1044248850028-csgv8o025t0cf5u68kgfolvespii9j7b.apps.googleusercontent.com";
            clientSecret = "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";
            redirectUri += "google";
        } else if (provider == 2) { // 카카오
            tokenEndpoint = "https://kauth.kakao.com/oauth/token";
            clientId = "YOUR_KAKAO_CLIENT_ID";
            clientSecret = "YOUR_KAKAO_CLIENT_SECRET";
            redirectUri += "kakao";
        } else if (provider == 3) { // 네이버
            tokenEndpoint = "https://nid.naver.com/oauth2.0/token";
            clientId = "YOUR_NAVER_CLIENT_ID";
            clientSecret = "YOUR_NAVER_CLIENT_SECRET";
            redirectUri += "naver";
        }

        // 토큰 요청 로직 (RestTemplate 또는 WebClient 사용)
        // 이 부분은 간단히 설명했지만 실제로는 HTTP 요청을 통해 서버에 코드를 보내고, 액세스 토큰을 받아야 합니다.
        return "FAKE_ACCESS_TOKEN"; // 여기서는 간단히 가짜 토큰을 반환합니다.
    }

    private OAuth2UserInfo getUserInfoFromProvider(String accessToken, int provider) {
        // provider에 따라 구글, 카카오, 네이버의 사용자 정보 엔드포인트에 접근하여 사용자 정보를 가져옴
        // 여기서도 실제로는 HTTP 요청을 통해 사용자 정보를 가져와야 합니다.
        return new OAuth2UserInfo("fakeEmail@example.com", "Fake Name", "https://fakeimage.url/profile.jpg", "1234567890");
    }
}
