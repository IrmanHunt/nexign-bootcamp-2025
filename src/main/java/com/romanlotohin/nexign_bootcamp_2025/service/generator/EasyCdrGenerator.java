package com.romanlotohin.nexign_bootcamp_2025.service.generator;

import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Класс для генерации простых записей о звонках CDR.
 * Реализация интерфейса {@link CdrGenerator
 */
@Component
public class EasyCdrGenerator implements CdrGenerator {
    private final CdrRecordRepository cdrRecordRepository;

    /**
     * Конструктор для инициализации генератора записей о звонках
     *
     * @param cdrRecordRepository репозиторий записей CDR
     */
    public EasyCdrGenerator(CdrRecordRepository cdrRecordRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
    }

    /**
     * Генерирует записи о звонках для списка абонентов за заданный год и интервал дней.
     * Записи создаются с фиксированными интервалами между звонками. Каждый звонок случайным образом
     * связывает двух абонентов, с заданной продолжительностью звонка и типом.
     * Звонки не могут пересекаться
     *
     * @param subscribers список абонентов, для которых генерируются записи
     * @param generationYear год, для которого генерируются записи
     * @param daysInterval интервал в днях между звонками
     */
    @Override
    public void generateCdrRecords(List<Subscriber> subscribers, int generationYear, int daysInterval) {
        Random random = new Random();

        int randomSeconds = random.nextInt(60);
        int randomMinutes = random.nextInt(10);

        LocalDateTime startOfYear = LocalDateTime.of(generationYear, 1, 1, 0, randomMinutes, randomSeconds);

        LocalDateTime endOfYear = startOfYear.plusYears(1);

        LocalDateTime currentTime = startOfYear;
        while (currentTime.isBefore(endOfYear)) {

            Subscriber caller = subscribers.get(random.nextInt(subscribers.size()));
            Subscriber callee = subscribers.get(random.nextInt(subscribers.size()));

            while (callee.getMsisdn().equals(caller.getMsisdn())) {
                callee = subscribers.get(random.nextInt(subscribers.size()));
            }

            String callType = random.nextBoolean() ? "01" : "02";

            LocalDateTime callStart = currentTime;

            randomSeconds = random.nextInt(60);
            randomMinutes = 1 + random.nextInt(10);

            LocalDateTime callEnd = callStart.plusMinutes(randomMinutes).plusSeconds(randomSeconds);


            CdrRecord record = new CdrRecord(callType, caller.getMsisdn(), callee.getMsisdn(), callStart, callEnd);
            cdrRecordRepository.save(record);

            currentTime = callEnd.plusDays(daysInterval).plusMinutes(random.nextInt(30));
        }
    }
}
