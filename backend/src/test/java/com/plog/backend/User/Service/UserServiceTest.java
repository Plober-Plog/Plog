package com.plog.backend.User.Service;

import com.plog.backend.domain.user.dto.RequestSignUpDto;
import com.plog.backend.domain.user.entity.*;
import com.plog.backend.domain.user.repository.UserRepository;
import com.plog.backend.domain.user.service.UserService;
import com.plog.backend.domain.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.plog.backend.domain.user.entity.Gender.gender;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("유저 등록")
    class AddUser {
        private RequestSignUpDto requestSignUpDto;

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {
            @BeforeEach
            void setUp() throws ParseException {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date birthDate = dateFormat.parse("2024-08-01");
                requestSignUpDto = new RequestSignUpDto().builder()
                        .profile("imageUrl")
                        .email("gapple95@naver.com")
                        .gender(1)
                        .searchId("gapple95")
                        .source("친구따라 강남간다.")
                        .birthDate(birthDate)
                        .nickname("gapple")
                        .password("password")
                        .sidoCode(10)
                        .gugunCode(20)
                        .build();
            }

            @Test
            @DisplayName("Service : 회원가입 성공")
            void addUserSuccess() {
                // given - then
                User user = User.builder()
                        .email(requestSignUpDto.getEmail())
//                        .gender(requestSignUpDto.getGender())
                        .gender(requestSignUpDto.getGender())
                        .role(1)
                        .state(1)
                        .profileInfo("안녕하세용")
                        .isAd(requestSignUpDto.isAd())
                        .totalExp(0)
                        .chatAuth(1)
                        .searchId(requestSignUpDto.getSearchId())
                        .password(requestSignUpDto.getPassword())
                        .build();

                // 에상되는 결과 객체 생성
                User mockUser = new User().builder()
                        .gender(1)
                        .role(1)
                        .state(1)
                        .chatAuth(1)
                        .build();

                when(userRepository.save(any(User.class))).thenReturn(mockUser);

                //when
                User result = userService.createUser(requestSignUpDto);

                //then
                assertThat(result).isNotNull();
                assertThat(result.getRole()).isEqualTo(Role.USER);
                assertThat(result.getState()).isEqualTo(State.ACTIVTE);
                assertThat(result.getChatAuth()).isEqualTo(ChatAuth.PUBLIC);
            }
        }
    }
}
