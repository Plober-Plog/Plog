package com.plog.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
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
}