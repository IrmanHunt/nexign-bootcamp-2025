package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanlotohin.nexign_bootcamp_2025.dto.ReportRequest;
import com.romanlotohin.nexign_bootcamp_2025.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().findAndRegisterModules().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGenerateCdrReport() throws Exception {
        ReportRequest request = new ReportRequest();
        request.setMsisdn("1234567890");
        request.setStart(LocalDate.of(2025, 1, 1));
        request.setEnd(LocalDate.of(2025, 1, 31));

        String expectedUuid = "test-uuid-1234";
        when(reportService.generateCdrReport(any(ReportRequest.class))).thenReturn(expectedUuid);

        mockMvc.perform(post("/api/reports/cdr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Отчет успешно сохранен. UUID запроса: " + expectedUuid));
    }
}
