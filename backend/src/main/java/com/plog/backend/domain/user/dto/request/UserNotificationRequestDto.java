package com.plog.backend.domain.user.dto.request;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class UserNotificationRequestDto {
    private String accessToken;
    private String notificationToken;
}
