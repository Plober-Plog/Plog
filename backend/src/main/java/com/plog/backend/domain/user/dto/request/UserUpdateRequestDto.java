package com.plog.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "검색 ID는 필수 입력 값입니다.")
    private String searchId;
    private String profile;
    private int gender;
    private Date birthDate;
    private String source;
    private int sidoCode;
    private int gugunCode;
    private String profileInfo;
    private boolean isAd;
}
