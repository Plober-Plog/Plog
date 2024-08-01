package com.plog.backend.domain.user.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SuccessMessage {

    SUCCESS_SIGNUP_USER("회원가입 성공"),
    SUCCESS_LOGIN_USER("로그인 성공"),
    SUCCESS_AUTH("허가"),
    SUCCESS_DELETE_USER("회원탈퇴 성공");

    private final String message;
}