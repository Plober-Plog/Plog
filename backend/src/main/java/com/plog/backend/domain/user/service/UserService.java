package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.dto.RequestSignUpDto;
import com.plog.backend.domain.user.entity.User;
import org.springframework.security.core.AuthenticationException;

public interface UserService {
    User getUserBySearchId(String searchId);
    String login(String email, String password);
    User createUser(RequestSignUpDto requestSignUpDto);
}
