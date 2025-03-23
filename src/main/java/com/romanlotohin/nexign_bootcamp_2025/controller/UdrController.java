package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.service.UdrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки запросов по данным использования UDR.
 * Контроллер предоставляет два эндпоинта:
 * 1. Для получения данных по UDR для конкретного абонента
 * 2. Для получения всех записей UDR для всех абонентов за указанный месяц
 */
@Slf4j
@RestController
@RequestMapping("/api/udr")
public class UdrController {
    private final UdrService udrService;
    private final int generationYear;

    /**
     * Конструктор контроллера
     *
     * @param udrService сервис для получения данных UDR
     * @param generationYear год для генерации данных
     */
    public UdrController(UdrService udrService, @Value("${app.generation.year}") int generationYear) {
        this.udrService = udrService;
        this.generationYear = generationYear;
    }

    /**
     * Эндпоинт для получения данных UDR для конкретного абонента
     *
     * @param msisdn номер телефона абонента
     * @param month месяц, за который необходимо получить данные (опциональный параметр)
     * @return ResponseEntity с объектом UdrDto
     */
    @GetMapping("/{msisdn}")
    public ResponseEntity<UdrDto> getUdrForSubscriber(
            @PathVariable String msisdn,
            @RequestParam(required = false) String month) {

        UdrDto udr = udrService.getUdrForSubscriber(msisdn, month, generationYear);
        return ResponseEntity.ok(udr);
    }

    /**
     * Эндпоинт для получения всех записей UDR для всех абонентов за указанный месяц
     *
     * @param month месяц, за который необходимо получить данные
     * @return ResponseEntity с списком объектов UdrDto
     */
    @GetMapping
    public ResponseEntity<List<UdrDto>> getAllUdrForMonth(@RequestParam String month) {
        List<UdrDto> udrList = udrService.getUdrForAllSubscribers(month, generationYear);
        log.info("Найдено {} записей UDR для месяца {}", udrList.size(), month);
        return ResponseEntity.ok(udrList);
    }
}
