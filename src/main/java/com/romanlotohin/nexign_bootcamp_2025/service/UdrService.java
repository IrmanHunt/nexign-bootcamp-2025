package com.romanlotohin.nexign_bootcamp_2025.service;

import com.romanlotohin.nexign_bootcamp_2025.dto.CallDuration;
import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UdrService {
    private final CdrRecordRepository cdrRecordRepository;
    private final SubscriberRepository subscriberRepository;

    public UdrService(CdrRecordRepository cdrRecordRepository, SubscriberRepository subscriberRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
        this.subscriberRepository = subscriberRepository;
    }

    public UdrDto getUdrForSubscriber(String msisdn, String month) {
        LocalDateTime callStart;
        LocalDateTime callEnd;

        if (month != null && !month.isEmpty()) {
            callStart = LocalDateTime.parse(month + "-01T00:00:00");
            callEnd = callStart.plusMonths(1);
        } else {
            callStart = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
            callEnd = LocalDateTime.of(2070, 1, 1, 0, 0, 0);
        }

        List<CdrRecord> records = cdrRecordRepository.findByMsisdnAndCallStartBetween(msisdn, callStart, callEnd);

        Duration incoming = Duration.ZERO;
        Duration outgoing = Duration.ZERO;

        for (CdrRecord record : records) {
            Duration callDuration = Duration.between(record.getCallStart(), record.getCallEnd());
            if ("01".equals(record.getCallType()) && record.getCallerMsisdn().equals(msisdn)) {
                outgoing = outgoing.plus(callDuration);
            } else if ("02".equals(record.getCallType()) && record.getCalleeMsisdn().equals(msisdn)) {
                incoming = incoming.plus(callDuration);
            }
        }

        return new UdrDto(msisdn, new CallDuration(formatDuration(incoming)), new CallDuration(formatDuration(outgoing)));
    }

    public List<UdrDto> getUdrForAllSubscribers(String month) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        return subscribers.stream()
                .map(subscriber -> getUdrForSubscriber(subscriber.getMsisdn(), month))
                .collect(Collectors.toList());
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
