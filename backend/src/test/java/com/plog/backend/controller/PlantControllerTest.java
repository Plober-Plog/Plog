package com.plog.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.backend.domain.diary.service.PlantDiaryService;
import com.plog.backend.domain.plant.controller.PlantController;
import com.plog.backend.domain.plant.dto.response.PlantGetResponseDto;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.auth.PloberUserDetailService;
import com.plog.backend.global.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlantController.class)
@AutoConfigureMockMvc
public class PlantControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private PlantService plantService;

    @MockBean
    private PlantDiaryService plantDiaryService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private PloberUserDetailService userDetailsService;

    @Autowired
    PlantControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetPlant() throws Exception {
        when(plantService.getPlant(1L)).thenReturn(new PlantGetResponseDto());

        mockMvc.perform(get("/user/plant/1/info"))
                .andExpect(status().isOk());
    }

//    @Nested
//    @DisplayName("식물 등록")
//    class AddPlant {
//
//        PlantAddRequestDto plantAddRequest;
//
//        @Nested
//        @DisplayName("정상 케이스")
//        class SuccessCase {
//
//            @BeforeEach
//            void setUp() throws ParseException {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date birthDate = dateFormat.parse("2024-07-30");
//                plantAddRequest = new PlantAddRequestDto().builder()
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
////                when(plantService.addPlant(any(PlantRequestDto.class))).thenReturn();
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
////                verify(plantService).addPlant(any(PlantAddRequest.class)); // service method 호출 확인
//            }
//        }
//
////        @Nested
////        @DisplayName("비정상 케이스")
////        class FailureCase {
////
////            @BeforeEach
////            void setUp() throws ParseException {
////                plantAddRequest = new PlantAddRequest().builder()
////                        .profile("imageUrl")
////                        .hasNotified(false)
////                        .isFixed(false).build();
////            }

////            @Test
////            @WithMockUser(username = "user", roles = {"USER"})
////            @DisplayName("닉네임 누락으로 등록 실패")
////            void failWhenNicknameIsMissing() throws Exception {
////                // given
////                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
////                Date birthDate = dateFormat.parse("2024-07-30");
////                plantAddRequest.setBirthDate(birthDate);
////
////                // when - then
////                mockMvc.perform(MockMvcRequestBuilders.post("/user/plant")
////                                .contentType(MediaType.APPLICATION_JSON)
////                                .content(objectMapper.writeValueAsString(plantAddRequest))
////                                .with(csrf()))
////                        .andExpect(status().isBadRequest())
////                        .andExpect(jsonPath("$.httpMethod").value("POST"))
////                        .andExpect(jsonPath("$.requestURL").value("/user/plant"))
////                        .andExpect(jsonPath("$.httpStatus").value(400))
////                        .andExpect(jsonPath("$.message").value("nickname 은 필수 필드입니다."))
////                        .andExpect(jsonPath("$.timestamp").exists());
////            }
////
////            @Test
////            @WithMockUser(username = "user", roles = {"USER"})
////            @DisplayName("생일 누락으로 등록 실패")
////            void failWhenBirthDateIsMissing() throws Exception {
////                // given
////                plantAddRequest.setNickname("nickname");
////
////                // when - then
////                mockMvc.perform(MockMvcRequestBuilders.post("/user/plant")
////                                .contentType(MediaType.APPLICATION_JSON)
////                                .content(objectMapper.writeValueAsString(plantAddRequest))
////                                .with(csrf()))
////                        .andExpect(status().isBadRequest())
////                        .andExpect(jsonPath("$.httpMethod").value("POST"))
////                        .andExpect(jsonPath("$.requestURL").value("/user/plant"))
////                        .andExpect(jsonPath("$.httpStatus").value(400))
////                        .andExpect(jsonPath("$.message").value("birthDate 는 필수 필드입니다."))
////                        .andExpect(jsonPath("$.timestamp").exists());
////            }
////        }
//    }


//    @Nested
//    @DisplayName("식물 조회")
//    class GetPlant {
//        @Nested
//        @DisplayName("정상 케이스")
//        class SuccessCase {
//            @BeforeEach
//            void setUp() throws ParseException {
//
//            }
//
//            @ParameterizedTest
//            @ValueSource(longs = {1L}) // 테스트에 사용할 plantId 값들
//            @WithMockUser(username = "user", roles = {"USER"})
//            @DisplayName("유효한 요청(plantId)으로 조회 성공")
//            void getPlantSuccessfully(Long plantId) throws Exception {
//                mockMvc.perform(MockMvcRequestBuilders.get("/user/plant/{plantId}/info", plantId))
//                        .andExpect(status().isOk());
//            }
//        }
//    }
}
