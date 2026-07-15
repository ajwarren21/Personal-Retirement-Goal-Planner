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
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.ContributionDto;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.enums.ContributionCategory;
import com.skillstorm.services.ContributionService;

@WebMvcTest(ContributionController.class)
class ContributionControllerTest {

    private static final String USERNAME = "testuser";

    private static final Principal PRINCIPAL = () -> USERNAME;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContributionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ContributionDto dto;
    private ResponseContributionDto responseDto;

    @BeforeEach
    void setUp() {

        dto = new ContributionDto(
            1L, // retirementGoalId
            2L, // fundingSourceId
            BigDecimal.valueOf(5000),
            LocalDate.now(),
            ContributionCategory.CATCH_UP_CONTRIBUTION,
            "notes"
        );

//           long id,
//     long retirementGoalId,
//     String retirementGoalName,
//     long fundingSourceId,
//     String fundingSourceName,
//     java.math.BigDecimal amount,
//     java.time.LocalDate contributionDate,
//     com.skillstorm.enums.ContributionCategory category,
//     String notes
// ) {}

        responseDto = new ResponseContributionDto(
            1L,
            1L,
            "goal",
            2L,
            "source",
            BigDecimal.valueOf(5000),
            LocalDate.now(),
            ContributionCategory.CATCH_UP_CONTRIBUTION,
            "notes"
        );
    }

    @Test
    @WithMockUser(username = USERNAME)
    void getAll_ShouldReturn200() throws Exception {

        when(service.getContributionsByUser(USERNAME))
                .thenReturn(java.util.List.of(responseDto));

        mockMvc.perform(get("/contributions").principal(PRINCIPAL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void getById_ShouldReturn200() throws Exception {

        when(service.getByIdForUser(1L, USERNAME))
                .thenReturn(responseDto);

        mockMvc.perform(get("/contributions/1").principal(PRINCIPAL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void create_ShouldReturn201() throws Exception {

        when(service.createContributionForUser(any(ContributionDto.class), eq(USERNAME)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/contributions")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void update_ShouldReturn200() throws Exception {

        when(service.updateForUser(eq(1L), any(ContributionDto.class), eq(USERNAME)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/contributions/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME)
    void delete_ShouldReturn204() throws Exception {

        doNothing().when(service).deleteForUser(1L, USERNAME);

        mockMvc.perform(delete("/contributions/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}