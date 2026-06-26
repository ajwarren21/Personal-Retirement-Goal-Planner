package com.skillstorm.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.models.Contribution;
import com.skillstorm.models.User;
import com.skillstorm.services.RetirementGoalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RetirementGoalController.class)
class RetirementGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetirementGoalService service;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private RetirementGoalDto requestDto;
    private ResponseRetirementGoalDto responseDto;

    @BeforeEach
    void setUp() {
        user = new User();

        requestDto = new RetirementGoalDto(
            user,
            "name",
            60,
            BigDecimal.valueOf(5000),
            List.of(new Contribution()), 
            "notes"
        );

        responseDto = mock(ResponseRetirementGoalDto.class);
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
        when(service.create(any(RetirementGoalDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }


    @Test
    void update_ShouldReturn200() throws Exception {
        when(service.update(eq(1L), any(RetirementGoalDto.class))).thenReturn(responseDto);

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