package com.plog.backend.User.Repository;

import com.plog.backend.domain.user.entity.*;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.repository.UserRepositorySupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(UserRepositoryTest.Config.class)
public class UserRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

    @TestConfiguration
    static class Config {
        @Bean
        public UserRepositorySupport userRepositorySupport() {
            return new UserRepositorySupport();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositorySupport userRepositorySupport;

//    @Test
//    @DisplayName("사용자 추가")
//    void addUser() {
//        // given
//        User user = new User().builder()
//                .email("gapple95@naver.com")
//                .searchId("gapple95")
//                .nickname("gapple")
//                .password("1234")
//                .gender(1)
//                .state(1)
//                .role(1)
//                .totalExp(0)
//                .chatAuth(1)
//                .profileInfo("")
//                .build();
//
//        // when
//        User savedUser = userRepository.save(user);
//
//        // then
//        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
//        log.info("savedUser: {}", savedUser);
//    }

//    @Test
//    @DisplayName("사용자 조회")
//    void findByUsername() {
//        // given
//        User user = new User(
//                "gapple95@naver.com",
//                "gapple95",
//                "gapple",
//                "1234",
//                Gender.NA,
//                State.ACTIVTE,
//                Role.USER,
//                0,
//                ChatAuth.PUBLIC_NEIGHBOR,
//                "");
//
//        userRepository.save(user);
//
//        // when
//        User savedUser = userRepositorySupport.findBySearchId(user.getSearchId());
//
//        // then
//        assertThat(savedUser.getSearchId()).isEqualTo(user.getSearchId());
//    }
}
