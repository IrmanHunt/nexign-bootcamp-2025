package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.service.UdrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/udr")
public class UdrController {
    private final UdrService udrService;

    public UdrController(UdrService udrService) {
        this.udrService = udrService;
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<UdrDto> getUdrForSubscriber(
            @PathVariable String msisdn,
            @RequestParam(required = false) String month) {
        UdrDto udr = udrService.getUdrForSubscriber(msisdn, month);
        return ResponseEntity.ok(udr);
    }

    @GetMapping
    public ResponseEntity<List<UdrDto>> getAllUdrForMonth(@RequestParam String month) {
        List<UdrDto> udrList = udrService.getUdrForAllSubscribers(month);
        return ResponseEntity.ok(udrList);
    }
}
