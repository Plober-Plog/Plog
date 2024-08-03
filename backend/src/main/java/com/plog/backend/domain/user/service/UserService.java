package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.request.UserUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignUpRequestDto;
import com.plog.backend.domain.user.dto.response.UserResponseDto;
import com.plog.backend.domain.user.entity.User;

public interface UserService {
    User getUserBySearchId(String searchId);
    UserResponseDto getUser(String token);
    String userSignIn(String email, String password);
    User createUser(UserSignUpRequestDto userSignUpRequestDto);
    Boolean checkUserSearchId(String searchId);
    Boolean checkUserEmail(String email);
    User updateUser(String token, UserUpdateRequestDto request);
    void deleteUser(String token);
    UserResponseDto findPassowrd(String email);
}
