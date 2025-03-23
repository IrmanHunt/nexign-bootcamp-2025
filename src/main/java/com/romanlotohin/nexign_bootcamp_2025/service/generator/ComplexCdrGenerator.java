package com.romanlotohin.nexign_bootcamp_2025.service.generator;

import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Класс для генерации сложных записей о звонках CDR.
 * Реализация интерфейса {@link CdrGenerator}
 */
@Component
public class ComplexCdrGenerator implements CdrGenerator {
    private final CdrRecordRepository cdrRecordRepository;

    /**
     * Конструктор для инициализации генератора записей о звонках
     *
     * @param cdrRecordRepository репозиторий записей CDR
     */
    public ComplexCdrGenerator(CdrRecordRepository cdrRecordRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
    }

    /**
     * Генерирует записи о звонках для списка абонентов за заданный год и интервал дней.
     * Записи создаются случайным образом, с учетом доступности абонентов и продолжительности звонков.
     * Звонки могут пересекаться друг с другом
     *
     * @param subscribers список абонентов, для которых генерируются записи
     * @param generationYear год, для которого генерируются записи
     * @param daysInterval интервал в днях между звонками
     */
    @Override
    public void generateCdrRecords(List<Subscriber> subscribers, int generationYear, int daysInterval) {
        Random random = new Random();

        LocalDateTime startOfYear = LocalDateTime.of(generationYear, 1, 1, random.nextInt(6), random.nextInt(60), random.nextInt(60));
        LocalDateTime endOfYear = startOfYear.plusYears(1);

        Map<String, LocalDateTime> availability = new HashMap<>();
        for (Subscriber sub : subscribers) {
            availability.put(sub.getMsisdn(), startOfYear);
        }

        LocalDateTime currentTime = startOfYear;
        while (currentTime.isBefore(endOfYear)) {

            Subscriber caller = getRandomAvailableSubscriber(subscribers, availability, currentTime, random, null);
            if (caller == null) {
                currentTime = currentTime.plusMinutes(1 + random.nextInt(30));
                continue;
            }
            String callerNumber = caller.getMsisdn();

            Subscriber callee = getRandomAvailableSubscriber(subscribers, availability, currentTime, random, callerNumber);
            if (callee == null) {
                currentTime = currentTime.plusMinutes(1 + random.nextInt(30));
                continue;
            }
            String calleeNumber = callee.getMsisdn();

            String callType = random.nextBoolean() ? "01" : "02";
            LocalDateTime callStart = currentTime;
            LocalDateTime callEnd = callStart.plus(generateCallDuration(random));

            CdrRecord record = new CdrRecord(callType, callerNumber, calleeNumber, callStart, callEnd);
            cdrRecordRepository.save(record);

            int delayMinutes = 10 + random.nextInt(600);
            availability.put(callerNumber, callEnd.plusMinutes(delayMinutes));
            availability.put(calleeNumber, callEnd.plusMinutes(delayMinutes));

            currentTime = currentTime.plusDays(daysInterval).plusMinutes(1 + random.nextInt(60)).plusSeconds(random.nextInt(60));
        }
    }

    /**
     * Выбирает случайного доступного абонента из списка
     *
     * @param subscribers список всех абонентов
     * @param availability map с временем доступности абонентов
     * @param currentTime текущее время для проверки доступности
     * @param random объект для случайного выбора
     * @param excludeMsisdn номер абонента, который не может быть выбран
     * @return выбранного абонента или null, если все абоненты недоступны
     */
    private Subscriber getRandomAvailableSubscriber(List<Subscriber> subscribers,
                                                    Map<String, LocalDateTime> availability,
                                                    LocalDateTime currentTime,
                                                    Random random,
                                                    String excludeMsisdn) {

        List<Subscriber> available = subscribers.stream()
                .filter(s -> (availability.get(s.getMsisdn()).isBefore(currentTime)
                        || availability.get(s.getMsisdn()).equals(currentTime))
                        && (excludeMsisdn == null || !s.getMsisdn().equals(excludeMsisdn)))
                .toList();

        if (available.isEmpty()) {
            return null;
        }

        return available.get(random.nextInt(available.size()));
    }

    /**
     * Генерирует продолжительность звонка.
     * Длительность звонка генерируется случайным образом с разными шансами на продолжительность
     *
     * @param random объект для случайного выбора
     * @return продолжительность звонка
     */
    private Duration generateCallDuration(Random random) {
        int chance = random.nextInt(100);
        int durationMinutes;
        int durationSeconds;

        if (chance < 80) {
            durationMinutes = 1 + random.nextInt(5);
            durationSeconds = random.nextInt(60);
        } else if (chance < 90) {
            durationMinutes = 0;
            durationSeconds = 10 + random.nextInt(21);
        } else {
            durationMinutes = 6 + random.nextInt(30);
            durationSeconds = random.nextInt(60);
        }

        return Duration.ofMinutes(durationMinutes).plusSeconds(durationSeconds);
    }
}
