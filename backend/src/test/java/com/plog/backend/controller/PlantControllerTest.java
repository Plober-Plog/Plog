package com.plog.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.backend.domain.plant.controller.PlantController;
import com.plog.backend.domain.plant.dto.request.PlantAddRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(PlantController.class)
@AutoConfigureMockMvc
public class PlantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("식물 등록")
    void canAddPlant() throws Exception {
        String request = objectMapper.writeValueAsString(new PlantAddRequest());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/plant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response: " + responseContent);
    }
}
