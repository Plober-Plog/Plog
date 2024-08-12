package com.plog.backend.global.auth.service;

import com.plog.backend.domain.image.entity.Image;
import com.plog.backend.domain.user.entity.ChatAuth;
import com.plog.backend.domain.user.entity.Role;
import com.plog.backend.domain.user.entity.State;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.global.auth.dto.OAuthAttributes;
import com.plog.backend.global.auth.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 구글, 네이버, 카카오
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        Optional<User> userOptional = userRepository.findByEmail(attributes.getEmail());

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setProvider(attributes.getProvider());
            user.setProviderId(attributes.getProviderId());
            user.setNickname(attributes.getName());
            user.setProfileInfo(attributes.getProfileImage());
        } else {
            user = User.builder()
                    .email(attributes.getEmail())
                    .searchId(generateSearchId(attributes.getEmail()))  // searchId 생성 로직 필요
                    .nickname(attributes.getName())
                    .password("oauth2") // OAuth2.0 사용 시 비밀번호는 의미가 없을 수 있음.
                    .provider(attributes.getProvider())
                    .providerId(attributes.getProviderId())
                    .image(new Image(attributes.getProfileImage()))
                    .role(Role.USER.getValue())
                    .state(State.ACTIVTE.getValue())
                    .totalExp(0)
                    .chatAuth(ChatAuth.PUBLIC.getValue())
                    .isPushNotificationEnabled(true)
                    .build();
        }

        return userRepository.save(user);
    }


    private String generateSearchId(String email) {
        // searchId를 이메일 기반으로 생성하는 로직 구현
        return email.split("@")[0];
    }
}
