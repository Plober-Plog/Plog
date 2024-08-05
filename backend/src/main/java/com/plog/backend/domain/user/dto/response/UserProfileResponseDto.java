package com.plog.backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDto {
    private String profile;
    private String nickname;
    private String profile_info;
    private String title;
    private int total_exp;
}
