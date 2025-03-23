package com.romanlotohin.nexign_bootcamp_2025.service;

import com.romanlotohin.nexign_bootcamp_2025.dto.ReportRequest;
import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private CdrRecordRepository cdrRecordRepository;

    @InjectMocks
    private ReportService reportService;

    @TempDir
    Path tempDir;

    private String reportDirectory;

    @BeforeEach
    public void setUp() {
        reportDirectory = tempDir.toAbsolutePath().toString();
        reportService = new ReportService(cdrRecordRepository, reportDirectory);
    }

    @Test
    public void testGenerateCdrReport_Success() throws IOException {
        ReportRequest request = new ReportRequest();
        request.setMsisdn("1234567890");
        request.setStart(LocalDate.of(2025, 1, 1));
        request.setEnd(LocalDate.of(2025, 1, 31));

        LocalDateTime callStart = request.getStart().atStartOfDay();
        LocalDateTime callEnd = request.getEnd().atStartOfDay();

        CdrRecord record1 = new CdrRecord("01", "1234567890", "other", callStart.plusHours(1), callStart.plusHours(2));
        CdrRecord record2 = new CdrRecord("02", "other", "1234567890", callStart.plusHours(3), callStart.plusHours(3).plusMinutes(30));
        List<CdrRecord> records = Arrays.asList(record1, record2);

        when(cdrRecordRepository.findByMsisdnAndCallStartBetween("1234567890", callStart, callEnd))
                .thenReturn(records);

        String uuid = reportService.generateCdrReport(request);

        File reportFile = new File(reportDirectory, "1234567890_" + uuid + ".csv");
        assertTrue(reportFile.exists(), "Нет файла с отчетом");
    }

    @Test
    public void testGenerateCdrReport_DirectoryCreationFails() {
        String invalidDirectory = "Z:/invalid/nonexistent/path";
        ReportService reportServiceInvalid = new ReportService(cdrRecordRepository, invalidDirectory);
        ReportRequest request = new ReportRequest();

        request.setMsisdn("1234567890");
        request.setStart(LocalDate.of(2025, 1, 1));
        request.setEnd(LocalDate.of(2025, 1, 31));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> reportServiceInvalid.generateCdrReport(request));

        assertTrue(ex.getReason().contains("Произошла ошибка при создании директории для отчетов"),
                "Проблема при создании директории");
    }
}
