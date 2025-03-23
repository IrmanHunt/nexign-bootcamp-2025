package com.romanlotohin.nexign_bootcamp_2025.service;

import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UdrServiceTest {

    @Mock
    private CdrRecordRepository cdrRecordRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private UdrService udrService;

    private final int generationYear = 2025;

    @Test
    public void testGetUdrForSubscriber_CalculateDurations() {
        String msisdn = "1234567890";
        String month = "april";

        LocalDateTime callStartExpected = LocalDateTime.of(generationYear, Month.APRIL, 1, 0, 0);
        LocalDateTime callEndExpected = callStartExpected.plusMonths(1);

        CdrRecord outgoingRecord = new CdrRecord(
                "01",
                msisdn,
                "other",
                callStartExpected.plusHours(1),
                callStartExpected.plusHours(2)
        );

        CdrRecord incomingRecord = new CdrRecord(
                "02",
                "other",
                msisdn,
                callStartExpected.plusHours(3),
                callStartExpected.plusHours(3).plusMinutes(30)
        );

        when(cdrRecordRepository.findByMsisdnAndCallStartBetween(eq(msisdn), eq(callStartExpected), eq(callEndExpected)))
                .thenReturn(Arrays.asList(outgoingRecord, incomingRecord));

        UdrDto result = udrService.getUdrForSubscriber(msisdn, month, generationYear);

        assertEquals(msisdn, result.getMsisdn());
        assertEquals("00:30:00", result.getIncomingCall().getTotalTime());
        assertEquals("01:00:00", result.getOutgoingCall().getTotalTime());
    }

    @Test
    public void testGetUdrForSubscriber_InvalidMonth_ThrowsException() {
        String msisdn = "1234567890";
        String invalidMonth = "Avril";

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                udrService.getUdrForSubscriber(msisdn, invalidMonth, generationYear)
        );

        String expectedMessage = "Неверное значение месяца";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetUdrForAllSubscribers() {
        String month = "april";

        Subscriber subscriber1 = new Subscriber("1111111111");
        Subscriber subscriber2 = new Subscriber("2222222222");
        when(subscriberRepository.findAll()).thenReturn(Arrays.asList(subscriber1, subscriber2));

        LocalDateTime callStartExpected = LocalDateTime.of(generationYear, Month.APRIL, 1, 0, 0);
        LocalDateTime callEndExpected = callStartExpected.plusMonths(1);
        when(cdrRecordRepository.findByMsisdnAndCallStartBetween(eq("1111111111"), eq(callStartExpected), eq(callEndExpected)))
                .thenReturn(Collections.emptyList());
        when(cdrRecordRepository.findByMsisdnAndCallStartBetween(eq("2222222222"), eq(callStartExpected), eq(callEndExpected)))
                .thenReturn(Collections.emptyList());

        List<UdrDto> resultList = udrService.getUdrForAllSubscribers(month, generationYear);

        assertEquals(2, resultList.size());

        for (UdrDto dto : resultList) {
            assertEquals("00:00:00", dto.getIncomingCall().getTotalTime());
            assertEquals("00:00:00", dto.getOutgoingCall().getTotalTime());
        }
    }
}
