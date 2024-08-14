package com.plog.backend.global.auth.Controller;

import com.plog.backend.domain.user.service.UserServiceImpl;
import com.plog.backend.global.auth.entity.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import org.springframework.web.util.UriComponentsBuilder;

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
        RestTemplate restTemplate = new RestTemplate();

        String tokenEndpoint = "";
        String clientId = "";
        String clientSecret = "";
        String redirectUri = "https://i11b308.p.ssafy.io/api/user/login/oauth2/code/";
//        String redirectUri = "http://localhost:8080/api/user/login/oauth2/code/";

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

        // 액세스 토큰 요청 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenEndpoint)
                .queryParam("code", code)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                entity,
                Map.class);

        Map<String, Object> responseBody = response.getBody();
        return (String) responseBody.get("access_token");
    }

    private OAuth2UserInfo getUserInfoFromProvider(String accessToken, int provider) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoEndpoint = "";

        if (provider == 1) { // 구글
            userInfoEndpoint = "https://www.googleapis.com/oauth2/v2/userinfo";
        } else if (provider == 2) { // 카카오
            userInfoEndpoint = "https://kapi.kakao.com/v2/user/me";
        } else if (provider == 3) { // 네이버
            userInfoEndpoint = "https://openapi.naver.com/v1/nid/me";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                entity,
                Map.class);

        Map<String, Object> responseBody = response.getBody();

        // 각 제공자별로 응답 데이터가 다르므로 이를 처리
        if (provider == 1) { // 구글
            return new OAuth2UserInfo(
                    (String) responseBody.get("email"),
                    (String) responseBody.get("name"),
                    (String) responseBody.get("picture"),
                    (String) responseBody.get("id"));
        } else if (provider == 2) { // 카카오
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return new OAuth2UserInfo(
                    (String) kakaoAccount.get("email"),
                    (String) profile.get("nickname"),
                    (String) profile.get("profile_image_url"),
                    String.valueOf(responseBody.get("id")));
        } else if (provider == 3) { // 네이버
            Map<String, Object> responseMap = (Map<String, Object>) responseBody.get("response");
            return new OAuth2UserInfo(
                    (String) responseMap.get("email"),
                    (String) responseMap.get("name"),
                    (String) responseMap.get("profile_image"),
                    (String) responseMap.get("id"));
        }

        return null; // 에러 처리 필요
    }
}
