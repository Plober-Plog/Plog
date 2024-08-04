package com.plog.backend.domain.user.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserPasswordUpdateRequestDto {
    private Long userId;
    private String password;
}
