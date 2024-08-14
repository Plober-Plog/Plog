package com.plog.backend.global.auth.entity;

import lombok.*;

@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class OAuth2UserInfo {
    private String email;
    private String name;
    private String profileImage;
    private String providerId;

    public OAuth2UserInfo(String email, String name, String profileImage, String providerId) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.providerId = providerId;
    }

    // getter methods
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getProviderId() {
        return providerId;
    }
}
