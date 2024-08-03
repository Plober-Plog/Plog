package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.request.UserUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignUpRequestDto;
import com.plog.backend.domain.user.entity.User;

public interface UserService {
    User getUserBySearchId(String searchId);
    String login(String email, String password);
    User createUser(UserSignUpRequestDto userSignUpRequestDto);
    Boolean checkUser(String searchId);
    Boolean checkEmail(String email);
    User updateUser(String token, UserUpdateRequestDto request);
}
