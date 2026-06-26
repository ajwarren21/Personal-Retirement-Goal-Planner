package com.skillstorm.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.enums.ContributionCategory;
import com.skillstorm.dtos.ContributionDto;
// import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.User;
import com.skillstorm.services.ContributionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ContributionController.class)
class ContributionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContributionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private RetirementGoal goal;
    private FundingSource source;
    private ContributionDto requestDto;
    private ResponseContributionDto responseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        goal = new RetirementGoal();
        goal.setId(1L);
        source = new FundingSource();
        source.setId(2L);
        // long id,
        // User user,
        // RetirementGoal goal,
        // FundingSource fundingSource,
        // BigDecimal amount,
        // LocalDate contributionDate,
        // ContributionCategory category,
        // String notes

        requestDto = new ContributionDto(
            user,
            goal.getId(),
            source.getId(),
            BigDecimal.valueOf(5000),
            LocalDate.now(),
            ContributionCategory.CATCH_UP_CONTRIBUTION, 
            "notes"
        );

        responseDto = mock(ResponseContributionDto.class);
    }


    @Test
    void getAll_ShouldReturn200() throws Exception {
        when(service.getAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk());
    }


    @Test
    void getById_ShouldReturn200() throws Exception {
        when(service.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/goals/1"))
                .andExpect(status().isOk());
    }


    @Test
    void create_ShouldReturn201() throws Exception {
        when(service.create(any(ContributionDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }


    @Test
    void update_ShouldReturn200() throws Exception {
        when(service.update(eq(1L), any(ContributionDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/goals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }


    @Test
    void delete_ShouldReturn200WithMessage() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/goals/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted dividend payment"));
    }
}