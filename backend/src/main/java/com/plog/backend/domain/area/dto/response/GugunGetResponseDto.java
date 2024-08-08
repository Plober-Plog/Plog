package com.plog.backend.domain.area.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GugunGetResponseDto {
    private int gugunCode;
    private String gugunName;
    private int sidoCode;
}
