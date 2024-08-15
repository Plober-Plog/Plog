package com.plog.backend.domain.user.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserProfileResponseDto {
    private String searchId;
    private String profile;
    private String nickname;
    private String profile_info;
    private String title;
    private int total_exp;
}
