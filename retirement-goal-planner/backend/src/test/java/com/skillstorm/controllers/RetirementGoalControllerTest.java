package com.skillstorm.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.models.Contribution;
import com.skillstorm.services.RetirementGoalService;

@WebMvcTest(RetirementGoalController.class)
class RetirementGoalControllerTest {

    private static final String USERNAME = "testuser";

    private static final Principal PRINCIPAL = () -> USERNAME;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetirementGoalService service;

    @Autowired
    private ObjectMapper objectMapper;

    private RetirementGoalDto dto;
    private ResponseRetirementGoalDto responseDto;

    @BeforeEach
    void setUp() {

        dto = new RetirementGoalDto(
            "Retire Early",
            60,
            BigDecimal.valueOf(500000),
            "Retire by age 60",
            List.<Contribution>of()
        );

//         public record ResponseRetirementGoalDto(
//     long id,
//     // User user,
//     String name,
//     Integer targetRetirementAge,
//     BigDecimal targetAmount,
//     List<ResponseContributionDto> contributions,
//     String notes
// ) {}

        responseDto = new ResponseRetirementGoalDto(
            1L,
            "Retire Early",
            60,
            BigDecimal.valueOf(500000),
            List.<ResponseContributionDto>of(),
            "Retire by age 60"
        );
    }

    @Test
    @WithMockUser(username = USERNAME)
    void getAll_ShouldReturn200() throws Exception {

        when(service.getGoalsByUser(USERNAME))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/goals").principal(PRINCIPAL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void getById_ShouldReturn200() throws Exception {

        when(service.getByIdForUser(1L, USERNAME))
                .thenReturn(responseDto);

        mockMvc.perform(get("/goals/1").principal(PRINCIPAL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void create_ShouldReturn201() throws Exception {

        when(service.createGoalForUser(any(RetirementGoalDto.class), eq(USERNAME)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/goals")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void update_ShouldReturn200() throws Exception {

        when(service.updateForUser(eq(1L), any(RetirementGoalDto.class), eq(USERNAME)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/goals/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void delete_ShouldReturn204() throws Exception {

        doNothing().when(service).deleteForUser(1L, USERNAME);

        mockMvc.perform(delete("/goals/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}