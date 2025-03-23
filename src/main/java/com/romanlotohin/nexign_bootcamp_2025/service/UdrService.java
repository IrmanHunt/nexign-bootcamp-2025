package com.romanlotohin.nexign_bootcamp_2025.service;

import com.romanlotohin.nexign_bootcamp_2025.dto.CallDuration;
import com.romanlotohin.nexign_bootcamp_2025.dto.UdrDto;
import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для получения UDR абонентов в переданных временных промежутках
 */
@Slf4j
@Service
public class UdrService {
    private final CdrRecordRepository cdrRecordRepository;
    private final SubscriberRepository subscriberRepository;

    /**
     * Конструктор для инициализации сервиса с зависимостями
     *
     * @param cdrRecordRepository репозиторий записей CDR
     * @param subscriberRepository репозиторий абонентов
     */
    public UdrService(CdrRecordRepository cdrRecordRepository, SubscriberRepository subscriberRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * Получение UDR для конкретного абонента за определенный месяц.
     * Метод рассчитывает продолжительность входящих и исходящих звонков для указанного абонента
     * в пределах заданного месяца и года. Если месяц не указан, рассматривается весь диапазон времени
     *
     * @param msisdn номер абонента
     * @param month месяц в виде строки (например, "aptil")
     * @param generationYear год, за который нужно сгенерировать отчет
     * @return DTO объект с данными о длительности входящих и исходящих звонков
     * @throws IllegalArgumentException если месяц указан неверно
     */
    public UdrDto getUdrForSubscriber(String msisdn, String month, int generationYear) {
        LocalDateTime callStart, callEnd;

        if (month != null && !month.isEmpty()) {
            Month monthEnum;
            try {
                monthEnum = Month.valueOf(month.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Неверное значение месяца: " + month);
            }
            callStart = LocalDateTime.of(generationYear, monthEnum, 1, 0, 0);
            callEnd = callStart.plusMonths(1);
        } else {
            callStart = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
            callEnd = LocalDateTime.of(2070, 1, 1, 0, 0, 0);
        }

        List<CdrRecord> records = cdrRecordRepository.findByMsisdnAndCallStartBetween(msisdn, callStart, callEnd);

        Duration incoming = Duration.ZERO, outgoing = Duration.ZERO;

        for (CdrRecord record : records) {
            Duration callDuration = Duration.between(record.getCallStart(), record.getCallEnd());
            if ("01".equals(record.getCallType()) && record.getCallerMsisdn().equals(msisdn)) {
                outgoing = outgoing.plus(callDuration);
            } else if ("02".equals(record.getCallType()) && record.getCalleeMsisdn().equals(msisdn)) {
                incoming = incoming.plus(callDuration);
            }
        }

        UdrDto udr = new UdrDto(msisdn, new CallDuration(formatDuration(incoming)), new CallDuration(formatDuration(outgoing)));
        log.info("UDR для абонента {}: {}", msisdn, udr);
        return udr;
    }

    /**
     * Получение UDR для всех абонентов за определенный месяц и год.
     * Метод возвращает список UDR для всех абонентов, включая их входящие и исходящие звонки
     * за указанный месяц и год
     *
     * @param month месяц в виде строки (например, "april")
     * @param generationYear год, за который нужно сгенерировать отчет
     * @return Список DTO объектов с данными о длительности входящих и исходящих звонков для каждого абонента
     */
    public List<UdrDto> getUdrForAllSubscribers(String month, int generationYear) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        return subscribers.stream()
                .map(subscriber -> getUdrForSubscriber(subscriber.getMsisdn(), month, generationYear))
                .collect(Collectors.toList());
    }

    /**
     * Форматирование длительности звонка в строковый формат "HH:mm:ss"
     *
     * @param duration длительность звонка
     * @return Строковое представление длительности в формате "HH:mm:ss"
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
