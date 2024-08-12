package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.request.*;
import com.plog.backend.domain.user.dto.response.UserCheckPasswordResponseDto;
import com.plog.backend.domain.user.dto.response.UserGetResponseDto;
import com.plog.backend.domain.user.dto.response.UserProfileResponseDto;
import com.plog.backend.domain.user.dto.response.UserPushResponseDto;
import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    User getUserBySearchId(String searchId);

    UserGetResponseDto getUser(String token);

    Map<String, String> userSignIn(String email, String password, String notificationToken);

    void userSignOut(String token);

    User createUser(UserSignUpRequestDto userSignUpRequestDto, String imageUrl);

    Boolean checkUserSearchId(String searchId);

    Boolean checkUserEmail(String email);
    User updateUser(String token, UserUpdateRequestDto request, MultipartFile[] profile);
    void deleteUser(String token);

    UserCheckPasswordResponseDto checkPassword(String token, UserPasswordCheckRequestDto userPasswordCheckRequestDto);

    void updatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto);

    UserProfileResponseDto getMyProfile(String token);

    UserProfileResponseDto getProfile(String searchId);

    UserPushResponseDto getPushUser(String token);

    User updatePushUser(String token, UserPushRequestDto userPushRequestDto);
}
