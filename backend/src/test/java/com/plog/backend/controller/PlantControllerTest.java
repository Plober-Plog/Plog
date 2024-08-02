package com.plog.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.backend.domain.plant.controller.PlantController;
import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlantController.class)
@AutoConfigureMockMvc
public class PlantControllerTest {

//    private final MockMvc mockMvc;
//    private final ObjectMapper objectMapper;
//
//    @MockBean
//    private PlantService plantService;
//
//    @Autowired
//    PlantControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
//        this.mockMvc = mockMvc;
//        this.objectMapper = objectMapper;
//    }
//
//    @Nested
//    @DisplayName("식물 등록")
//    class AddPlant {
//
//        PlantAddRequest plantAddRequest;
//
//        @Nested
//        @DisplayName("정상 케이스")
//        class SuccessCase {
//
//            @BeforeEach
//            void setUp() throws ParseException {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantAddRequest = new PlantAddRequest().builder()
//                        .profile("imageUrl")
//                        .nickname("nickname")
//                        .birthDate(birthDate)
//                        .hasNotified(false)
//                        .isFixed(false).build();
//            }
//
//            @Test
//            @WithMockUser(username = "user", roles = {"USER"})
//            @DisplayName("유효한 요청으로 등록 성공")
//            void addPlantSuccessfully() throws Exception {
//                // given
//                Plant plant = new Plant();
//                when(plantService.addPlant(any(PlantAddRequest.class))).thenReturn(plant);
//                BaseResponseBody expectedResponse = BaseResponseBody.of(200, "식물 등록이 완료되었습니다.");
//
//                // when - then
//                mockMvc.perform(MockMvcRequestBuilders.post("/user/plant")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(plantAddRequest))
//                                .with(csrf()))
//                        .andExpect(status().isOk())
//                        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
//
//                verify(plantService).addPlant(any(PlantAddRequest.class)); // service method 호출 확인
//            }
//        }
//
//        @Nested
//        @DisplayName("비정상 케이스")
//        class FailureCase {
//
//            @BeforeEach
//            void setUp() throws ParseException {
//                plantAddRequest = new PlantAddRequest().builder()
//                        .profile("imageUrl")
//                        .hasNotified(false)
//                        .isFixed(false).build();
//            }
//
//            //TODO [강윤서]
//            // - 권한이 없는 회원의 요청에 대한 401 예외 테스트
//
//            @Test
//            @WithMockUser(username = "user", roles = {"USER"})
//            @DisplayName("닉네임 누락으로 등록 실패")
//            void failWhenNicknameIsMissing() throws Exception {
//                // given
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantAddRequest.setBirthDate(birthDate);
//
//                // when - then
//                mockMvc.perform(MockMvcRequestBuilders.post("/user/plant")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(plantAddRequest))
//                                .with(csrf()))
//                        .andExpect(status().isBadRequest())
//                        .andExpect(jsonPath("$.httpMethod").value("POST"))
//                        .andExpect(jsonPath("$.requestURL").value("/user/plant"))
//                        .andExpect(jsonPath("$.httpStatus").value(400))
//                        .andExpect(jsonPath("$.message").value("nickname 은 필수 필드입니다."))
//                        .andExpect(jsonPath("$.timestamp").exists());
//            }
//
//            @Test
//            @WithMockUser(username = "user", roles = {"USER"})
//            @DisplayName("생일 누락으로 등록 실패")
//            void failWhenBirthDateIsMissing() throws Exception {
//                // given
//                plantAddRequest.setNickname("nickname");
//
//                // when - then
//                mockMvc.perform(MockMvcRequestBuilders.post("/user/plant")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(plantAddRequest))
//                                .with(csrf()))
//                        .andExpect(status().isBadRequest())
//                        .andExpect(jsonPath("$.httpMethod").value("POST"))
//                        .andExpect(jsonPath("$.requestURL").value("/user/plant"))
//                        .andExpect(jsonPath("$.httpStatus").value(400))
//                        .andExpect(jsonPath("$.message").value("birthDate 는 필수 필드입니다."))
//                        .andExpect(jsonPath("$.timestamp").exists());
//            }
//        }
//    }
}
