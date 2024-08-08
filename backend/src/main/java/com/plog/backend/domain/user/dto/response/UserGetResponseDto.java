package com.plog.backend.domain.user.dto.response;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGetResponseDto {
    private String email;
    private String searchId;
    private String nickname;
    private String profile;
    private int gender;
    private LocalDate birthDate;
    private int sidoCode;
    private int gugunCode;
    private String profileInfo;
    private boolean isAd;
    private boolean isPushNotificationEnabled;
}
