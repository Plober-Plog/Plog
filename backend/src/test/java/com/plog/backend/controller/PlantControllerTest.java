package com.plog.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.backend.domain.plant.controller.PlantController;
import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import com.plog.backend.domain.plant.entity.Plant;
import com.plog.backend.domain.plant.service.PlantService;
import com.plog.backend.global.model.response.BaseResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(PlantController.class)
@AutoConfigureMockMvc
public class PlantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PlantService plantService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("식물 등록")
    void canAddPlant() throws Exception {
        // given
        PlantAddRequest requestDto = new PlantAddRequest();
        Plant plant = new Plant();

        // Mocking the service method
        when(plantService.addPlant(any(PlantAddRequest.class))).thenReturn(plant);

        // expected response body
        BaseResponseBody expectedResponse = BaseResponseBody.of(200, "식물 등록이 완료되었습니다.");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/plant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));

        // then
        verify(plantService).addPlant(any(PlantAddRequest.class));  // Verify the service method was called
    }
}
