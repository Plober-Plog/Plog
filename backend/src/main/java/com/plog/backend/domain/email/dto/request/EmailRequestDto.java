package com.plog.backend.domain.email.dto.request;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
public class EmailRequestDto {
    // 이메일 주소
    private String email;
}
