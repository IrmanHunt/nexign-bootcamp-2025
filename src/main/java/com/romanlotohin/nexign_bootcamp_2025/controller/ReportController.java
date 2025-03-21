package com.romanlotohin.nexign_bootcamp_2025.controller;

import com.romanlotohin.nexign_bootcamp_2025.dto.ReportRequest;
import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final CdrRecordRepository cdrRecordRepository;

    public ReportController(CdrRecordRepository cdrRecordRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
    }

    @Value("${app.report.directory:./reports}")
    private String reportDirectory;

    @PostMapping("/cdr")
    public ResponseEntity<String> generateCdrReport(@RequestBody ReportRequest request) {
        String msisdn = request.getMsisdn();
        LocalDateTime callStart = request.getStart().atStartOfDay();
        LocalDateTime callEnd = request.getEnd().atStartOfDay();

        String uuid = UUID.randomUUID().toString();
        List<CdrRecord> records = cdrRecordRepository.findByMsisdnAndCallStartBetween(msisdn, callStart, callEnd);

        String filename = msisdn + "_" + uuid + ".csv";
        File dir = new File(reportDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Произошла ошибка при создании директории для отчетов");
            }
        }
        File reportFile = new File(dir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write("callType,callerMsisdn,calleeMsisdn,callStart,callEnd");
            writer.newLine();
            for (CdrRecord record : records) {
                writer.write(String.join(",",
                        record.getCallType(),
                        record.getCallerMsisdn(),
                        record.getCalleeMsisdn(),
                        record.getCallStart().toString(),
                        record.getCallEnd().toString()));
                writer.newLine();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании отчета: " + e.getMessage());
        }

        return ResponseEntity.ok("Отчет успешно сохранен. UUID запроса: " + uuid);
    }
}
