package com.plog.backend.domain.email.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
public class RequestEmailDto {
    // 이메일 주소
    private String mail;
    // 인증 코드
    private String verifyCode;
}
