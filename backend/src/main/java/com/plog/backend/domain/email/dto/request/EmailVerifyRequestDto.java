package com.plog.backend.domain.email.dto.request;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
public class EmailVerifyRequestDto {
    // 이메일 주소
    private String email;
    // 인증 코드
    private String verifyCode;
}
