package com.plog.backend.global.auth;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import com.plog.backend.domain.user.service.UserService;
import com.plog.backend.domain.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PloberUserDetailService implements UserDetailsService {
    private final UserRepositorySupport userRepositorySupport;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositorySupport.findByEmail(username);
        if (user != null) {
            return new PloberUserDetails(user);
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
