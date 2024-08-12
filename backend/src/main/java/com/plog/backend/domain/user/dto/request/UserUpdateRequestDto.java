package com.plog.backend.domain.user.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String nickname;
    private String searchId;
    private MultipartFile profile;
    private int gender;
    private LocalDate birthDate;
    private String source;
    private int sidoCode;
    private int gugunCode;
    private String profileInfo;
    private boolean isAd;
}
