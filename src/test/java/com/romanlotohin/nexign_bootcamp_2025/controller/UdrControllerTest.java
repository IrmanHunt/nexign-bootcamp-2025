package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanlotohin.nexign_bootcamp_2025.dto.CallDuration;
import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.service.UdrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UdrController.class)
public class UdrControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UdrService udrService;

    @Value("${app.generation.year}")
    private int generationYear;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetUdrForSubscriber() throws Exception {
        String msisdn = "79876543221";
        String month = "april";

        UdrDto dto = new UdrDto(msisdn, new CallDuration("01:30:00"), new CallDuration("00:45:00"));

        when(udrService.getUdrForSubscriber(eq(msisdn), eq(month), eq(generationYear))).thenReturn(dto);

        mockMvc.perform(get("/api/udr/{msisdn}", msisdn)
                        .param("month", month))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msisdn").value(msisdn))
                .andExpect(jsonPath("$.incomingCall.totalTime").value("01:30:00"))
                .andExpect(jsonPath("$.outgoingCall.totalTime").value("00:45:00"));
    }

    @Test
    public void testGetAllUdrForMonth() throws Exception {
        String month = "april";
        List<UdrDto> udrList = Arrays.asList(
                new UdrDto("1234567890", new CallDuration("01:00:00"), new CallDuration("00:30:00")),
                new UdrDto("0987654321", new CallDuration("00:45:00"), new CallDuration("00:15:00"))
        );

        when(udrService.getUdrForAllSubscribers(eq(month), eq(generationYear))).thenReturn(udrList);

        mockMvc.perform(get("/api/udr")
                        .param("month", month))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].msisdn").value("1234567890"))
                .andExpect(jsonPath("$[0].incomingCall.totalTime").value("01:00:00"))
                .andExpect(jsonPath("$[0].outgoingCall.totalTime").value("00:30:00"))
                .andExpect(jsonPath("$[1].msisdn").value("0987654321"))
                .andExpect(jsonPath("$[1].incomingCall.totalTime").value("00:45:00"))
                .andExpect(jsonPath("$[1].outgoingCall.totalTime").value("00:15:00"));
    }
}
