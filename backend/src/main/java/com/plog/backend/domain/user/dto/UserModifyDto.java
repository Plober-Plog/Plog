package com.plog.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyDto {
    @NotBlank(message = "검색 ID는 필수 입력 값입니다.")
    private String searchId;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    private String profile;

    private int gender;

    private Date birthDate;
    private String source;

    private boolean isAd;

    private int sidoCode;
    private int gugunCode;
}
