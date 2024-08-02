package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.UserModifyDto;
import com.plog.backend.domain.user.dto.UserSignUpDto;
import com.plog.backend.domain.user.entity.User;

public interface UserService {
    User getUserBySearchId(String searchId);
    String login(String email, String password);
    User createUser(UserSignUpDto userSignUpDto);
    Boolean checkUser(String searchId);
    Boolean checkEmail(String email);
    User updateUser(String token, UserModifyDto request);
}
