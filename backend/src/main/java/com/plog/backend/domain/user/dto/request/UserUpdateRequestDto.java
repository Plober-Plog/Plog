package com.plog.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String nickname;
    private String searchId;
    private String profile;
    private int gender;
    private LocalDate birthDate;
    private String source;
    private int sidoCode;
    private int gugunCode;
    private String profileInfo;
    private boolean isAd;
}
