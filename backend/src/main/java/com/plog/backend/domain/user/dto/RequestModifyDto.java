package com.plog.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class RequestModifyDto {
    private String nickname;
    private Date birthDate;
    private String gender;
    private String chatAuth;
    private String profileInfo;
    private String profileImageUrl;
    private String searchId;
}
