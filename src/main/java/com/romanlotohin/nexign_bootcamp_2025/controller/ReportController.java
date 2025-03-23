package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.romanlotohin.nexign_bootcamp_2025.dto.ReportRequest;
import com.romanlotohin.nexign_bootcamp_2025.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для генерации отчетов CDR.
 * Обрабатывает POST-запросы на эндпоинт /api/reports/cdr для генерации отчетов
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    /**
     * Конструктор контроллера
     *
     * @param reportService сервис для создания отчетов
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Эндпоинт для генерации отчета CDR
     *
     * @param request запрос с параметрами для генерации отчета
     * @return ResponseEntity с сообщением об успешной генерации отчета и UUID запроса
     */
    @PostMapping("/cdr")
    public ResponseEntity<String> generateCdrReport(@RequestBody ReportRequest request) {
        String uuid = reportService.generateCdrReport(request);
        log.info("Сгенерирован отчет с UUID: {}", uuid);
        return ResponseEntity.ok("Отчет успешно сохранен. UUID запроса: " + uuid);
    }
}
