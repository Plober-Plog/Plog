package com.plog.backend.domain.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponseDto {
    private boolean result;
    private String message;
}
