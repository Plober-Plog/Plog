package com.plog.backend.domain.user.service;

import com.plog.backend.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Override
    public User getUserBySearchId(String searchId) {
        return null;
    }
}
