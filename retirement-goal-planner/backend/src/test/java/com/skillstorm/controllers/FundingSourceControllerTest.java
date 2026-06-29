package com.skillstorm.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.services.FundingSourceService;

@WebMvcTest(FundingSourceController.class)
public class FundingSourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FundingSourceService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseFundingSourceDto responseDto;
    private FundingSourceDto dto;

    @BeforeEach
    void setUp() {


        responseDto = new ResponseFundingSourceDto(
            1L,
            "Roth IRA",
            "ROTH_IRA",
            "Fidelity",
            "Notes..."
        );

        dto = new FundingSourceDto(
            "Roth IRA",
            "ROTH_IRA",
            "Fidelity",
            "Notes..."
        ); 
    }


    @Test
    void getAll_ShouldReturn200() throws Exception {
        when(service.getAllFundingSources()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/funding-source")).andExpect(status().isOk());
    }

     @Test
    void getById_ShouldReturn200() throws Exception {
        when(service.getFundingSourceById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/funding-source/1"))
                .andExpect(status().isOk());
    }


    @Test
    void create_ShouldReturn201() throws Exception {
        when(service.createFundingSource(any(FundingSourceDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/funding-source")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }


    @Test
    void update_ShouldReturn200() throws Exception {
        when(service.updateFundingSource(eq(1L), any(FundingSourceDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/funding-source/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }


    @Test
    void delete_ShouldReturn200WithMessage() throws Exception {
        doNothing().when(service).deleteFundingSource(1L);

        mockMvc.perform(delete("/funding-source/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Funding Source Successfully"));
    }



}
