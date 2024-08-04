package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.request.UserPasswordCheckRequestDto;
import com.plog.backend.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserUpdateRequestDto;
import com.plog.backend.domain.user.dto.request.UserSignUpRequestDto;
import com.plog.backend.domain.user.dto.response.UserCheckPasswordResponseDto;
import com.plog.backend.domain.user.dto.response.UserGetResponseDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.response.BaseResponseBody;

public interface UserService {
    User getUserBySearchId(String searchId);
    UserGetResponseDto getUser(String token);
    String userSignIn(String email, String password);
    User createUser(UserSignUpRequestDto userSignUpRequestDto);
    Boolean checkUserSearchId(String searchId);
    Boolean checkUserEmail(String email);
    User updateUser(String token, UserUpdateRequestDto request);
    void deleteUser(String token);
    UserCheckPasswordResponseDto checkPassword(String token, UserPasswordCheckRequestDto userPasswordCheckRequestDto);
    void updatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto);
}
