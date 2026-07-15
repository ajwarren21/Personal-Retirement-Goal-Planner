package com.skillstorm.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

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
import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.services.FundingSourceService;

@WebMvcTest(FundingSourceController.class)
public class FundingSourceControllerTest {

    private static final String USERNAME = "testuser";

    // Simple lambda-based Principal — no Spring Security test dependency needed
    // since @WebMvcTest here doesn't load a security filter chain.
    private static final Principal PRINCIPAL = () -> USERNAME;

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
            "Notes...",
            List.<ResponseContributionDto>of()
        );

//         public record ResponseFundingSourceDto(
//     Long id,
//     String name,
//     String sourceType,
//     String institution,
//     String notes,
//     List<ResponseContributionDto> contributions
// ) {}

        dto = new FundingSourceDto(
            "Roth IRA",
            "ROTH_IRA",
            "Fidelity",
            "Notes..."
        );
    }

    @Test
    @WithMockUser(username = USERNAME)
    void getAll_ShouldReturn200() throws Exception {
        when(service.getSourcesByUser(USERNAME)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/funding-source").principal(PRINCIPAL))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME)
    @Test
    void getById_ShouldReturn200() throws Exception {
        when(service.getFundingSourceByIdForUser(1L, USERNAME)).thenReturn(responseDto);

        mockMvc.perform(get("/funding-source/1").principal(PRINCIPAL))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME)
    @Test
    void create_ShouldReturn201() throws Exception {
        when(service.createSourceForUser(any(FundingSourceDto.class), eq(USERNAME))).thenReturn(responseDto);

        mockMvc.perform(post("/funding-source")
                .with(csrf())
                // .principal(PRINCIPAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = USERNAME)
    @Test
    void update_ShouldReturn200() throws Exception {
        when(service.updateFundingSourceForUser(eq(1L), any(FundingSourceDto.class), eq(USERNAME)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/funding-source/1")
                // .principal(PRINCIPAL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME)
    @Test
    void delete_ShouldReturn204() throws Exception {
        doNothing().when(service).deleteFundingSourceForUser(1L, USERNAME);

        mockMvc.perform(delete("/funding-source/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}