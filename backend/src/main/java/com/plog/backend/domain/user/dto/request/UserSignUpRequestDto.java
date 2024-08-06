package com.plog.backend.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString(exclude = "profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequestDto {
    private String email;
    private String searchId;
    private String password;
    private String nickname;
    private int gender;
    @JsonProperty("birthDate")
    private LocalDate birthDate;
    private String source;
    private boolean isAd;
    private int sidoCode;
    private int gugunCode;

    private String profileInfo;
}
