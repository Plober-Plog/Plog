package com.plog.backend.User.Controller;

import com.plog.backend.domain.user.controller.UserController;
import com.plog.backend.domain.user.dto.RequestSignUpDto;
import com.plog.backend.domain.user.entity.*;
import com.plog.backend.domain.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserServiceImpl userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("회원가입 성공")
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void registerUser_success() throws Exception {
//        // given
//        RequestSignUpDto request = new RequestSignUpDto();
//        request.setEmail("gapple95@naver.com");
//        request.setSearchId("searchId");
//        request.setNickname("nickname");
//        request.setPassword("password");
//        request.setGender(1);
//
//        User user = User.builder()
//                .email(request.getEmail())
//                .searchId(request.getSearchId())
//                .nickname(request.getNickname())
//                .password("encodedPassword")
//                .gender(request.getGender())
//                .isAd(request.isAd())
//                .state(1)
//                .chatAuth(1)
//                .role(1)
//                .build();
//
//        // when
//        Mockito.when(userService.createUser(request))
//                .thenReturn(user);
//
//        // then
//        mockMvc.perform(post("/api/user")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // CSRF 토큰 추가
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
////                .andExpect(jsonPath("$.email").value(request.getEmail()))
////                .andExpect(jsonPath("$.searchId").value(request.getSearchId()))
////                .andExpect(jsonPath("$.nickname").value(request.getNickname()))
////                .andExpect(jsonPath("$.gender").value(request.getGender()));
//    }

//    @Test
//    @DisplayName("회원가입 실패 : SearchId 누락")
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void registerUser_missingFields() throws Exception {
//        // given
//        RequestSignUpDto request = new RequestSignUpDto();
//        request.setEmail("gapple95@naver.com");
//        request.setNickname("nickname");
//        request.setPassword("password");
//        request.setGender(1);
//
//        // when, then
//        mockMvc.perform(post("/api/user")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // CSRF 토큰 추가
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("회원가입 실패 : 이메일 형식")
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void registerUser_invalidEmail() throws Exception {
//        RequestSignUpDto request = new RequestSignUpDto();
//        request.setEmail("gapple95@naver.com");
//        request.setNickname("nickname");
//        request.setPassword("password");
//        request.setGender(1);
//
//        // when, then
//        mockMvc.perform(post("/api/user")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // CSRF 토큰 추가
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
}
