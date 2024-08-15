package com.plog.backend.domain.user.dto.request;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPushRequestDto {
    private boolean pushNotificationEnabled;
}
