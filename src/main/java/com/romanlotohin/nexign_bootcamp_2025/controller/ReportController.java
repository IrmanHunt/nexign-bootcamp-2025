package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.romanlotohin.nexign_bootcamp_2025.dto.ReportRequest;
import com.romanlotohin.nexign_bootcamp_2025.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/cdr")
    public ResponseEntity<String> generateCdrReport(@RequestBody ReportRequest request) {
        String uuid = reportService.generateCdrReport(request);
        log.info("Сгенерирован отчет с UUID: {}", uuid);
        return ResponseEntity.ok("Отчет успешно сохранен. UUID запроса: " + uuid);
    }
}
