package com.plog.backend.global.auth;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class PloberUserDetailService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserBySearchId(username);
        if(user != null) {
            PloberUserDetails userDetails = new PloberUserDetails(user);
            return userDetails;
        }
        return null;
    }
}
