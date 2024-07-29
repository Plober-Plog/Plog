package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.entity.User;

public interface UserService {
    User getUserBySearchId(String searchId);
}
