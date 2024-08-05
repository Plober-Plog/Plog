package com.plog.backend.domain.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VerifyResponseDto {
    private boolean result;
    private String message;
    private Long userId;
}
