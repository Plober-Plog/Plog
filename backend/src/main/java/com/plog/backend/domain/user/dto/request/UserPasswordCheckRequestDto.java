package com.plog.backend.domain.user.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserPasswordCheckRequestDto {
    private String password;
}
