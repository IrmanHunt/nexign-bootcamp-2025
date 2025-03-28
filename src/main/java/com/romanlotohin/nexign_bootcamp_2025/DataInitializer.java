package com.romanlotohin.nexign_bootcamp_2025;

import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.CdrGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Класс для инициализации данных при запуске приложения.
 * Загружает абонентов в репозиторий и генерирует CDR записи
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final SubscriberRepository subscriberRepository;
    private final CdrGenerator cdrGenerator;
    private final int generationYear;
    private final int daysInterval;

    /**
     * Конструктор для инициализации данных
     *
     * @param subscriberRepository репозиторий абонентов
     * @param cdrGenerator генератор CDR записей
     * @param generationYear год генерации CDR записей
     * @param daysInterval интервал между звонками в днях
     */
    public DataInitializer(SubscriberRepository subscriberRepository,
                           CdrGenerator cdrGenerator,
                           @Value("${app.generation.year}") int generationYear,
                           @Value("${app.call.interval.days:0}") int daysInterval) {
        this.subscriberRepository = subscriberRepository;
        this.cdrGenerator = cdrGenerator;
        this.generationYear = generationYear;
        this.daysInterval = daysInterval;
    }

    /**
     * Запускает процесс инициализации данных: сохраняет абонентов и генерирует CDR записи
     */
    @Override
    public void run(String... args) {

        List<Subscriber> subscribers = Arrays.asList(
                new Subscriber("79876543221"),
                new Subscriber("79123456782"),
                new Subscriber("79996667753"),
                new Subscriber("79887766554"),
                new Subscriber("79112223345"),
                new Subscriber("79005544336"),
                new Subscriber("79113334457"),
                new Subscriber("79887766558"),
                new Subscriber("79123334459"),
                new Subscriber("79884455610"),
                new Subscriber("79885566711"),
                new Subscriber("79114445512"),
                new Subscriber("79887766513"),
                new Subscriber("79121122314"),
                new Subscriber("79885566715")
        );
        subscriberRepository.saveAll(subscribers);

        cdrGenerator.generateCdrRecords(subscribers, generationYear, daysInterval);
    }
}
