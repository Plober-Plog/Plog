package com.plog.backend.domain.user.dto.response;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPushResponseDto {
    private boolean isPushNotificationEnabled;
}

