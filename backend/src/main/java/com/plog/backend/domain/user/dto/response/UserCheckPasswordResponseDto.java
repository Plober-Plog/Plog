package com.plog.backend.domain.user.dto.response;

import com.plog.backend.global.model.response.BaseResponseBody;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserCheckPasswordResponseDto {
    private Long userId = null;
    private Integer statusCode;
    private String message;

    public static UserCheckPasswordResponseDto of(Long userId, Integer status, String message) {
        UserCheckPasswordResponseDto dto = new UserCheckPasswordResponseDto();
        dto.setUserId(userId);
        dto.setStatusCode(status);
        dto.setMessage(message);
        return dto;
    }

}
