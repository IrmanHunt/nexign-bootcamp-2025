package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.service.UdrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/udr")
public class UdrController {
    private final UdrService udrService;
    private final int generationYear;

    public UdrController(UdrService udrService, @Value("${app.generation.year}") int generationYear) {
        this.udrService = udrService;
        this.generationYear = generationYear;
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<UdrDto> getUdrForSubscriber(
            @PathVariable String msisdn,
            @RequestParam(required = false) String month) {

        UdrDto udr = udrService.getUdrForSubscriber(msisdn, month, generationYear);
        return ResponseEntity.ok(udr);
    }

    @GetMapping
    public ResponseEntity<List<UdrDto>> getAllUdrForMonth(@RequestParam String month) {
        List<UdrDto> udrList = udrService.getUdrForAllSubscribers(month, generationYear);
        log.info("Найдено {} записей UDR для месяца {}", udrList.size(), month);
        return ResponseEntity.ok(udrList);
    }
}
