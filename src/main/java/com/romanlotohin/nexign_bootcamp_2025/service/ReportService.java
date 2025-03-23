package com.romanlotohin.nexign_bootcamp_2025.service;

import com.romanlotohin.nexign_bootcamp_2025.dto.ReportRequest;
import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {
    private final CdrRecordRepository cdrRecordRepository;
    private final String reportDirectory;

    public ReportService(CdrRecordRepository cdrRecordRepository,
                         @Value("${app.report.directory}") String reportDirectory) {
        this.cdrRecordRepository = cdrRecordRepository;
        this.reportDirectory = reportDirectory;
    }

    public String generateCdrReport(ReportRequest request) {
        String msisdn = request.getMsisdn();
        LocalDateTime callStart = request.getStart().atStartOfDay();
        LocalDateTime callEnd = request.getEnd().atStartOfDay();
        String uuid = UUID.randomUUID().toString();
        List<CdrRecord> records = cdrRecordRepository.findByMsisdnAndCallStartBetween(msisdn, callStart, callEnd);

        String filename = msisdn + "_" + uuid + ".csv";
        File dir = new File(reportDirectory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Произошла ошибка при создании директории для отчетов");
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка при создании отчета: " + e.getMessage());
        }

        return uuid;
    }
}
