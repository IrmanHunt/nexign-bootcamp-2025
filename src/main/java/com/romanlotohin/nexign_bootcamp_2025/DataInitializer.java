package com.romanlotohin.nexign_bootcamp_2025;

import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CdrRecordRepository cdrRecordRepository;
    private final SubscriberRepository subscriberRepository;

    public DataInitializer(CdrRecordRepository cdrRecordRepository, SubscriberRepository subscriberRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void run(String... args) {

        List<Subscriber> subscribers = Arrays.asList(
            new Subscriber("70000000001"),
            new Subscriber("70000000002"),
            new Subscriber("70000000003"),
            new Subscriber("70000000004"),
            new Subscriber("70000000005"),
            new Subscriber("70000000006"),
            new Subscriber("70000000007"),
            new Subscriber("70000000008"),
            new Subscriber("70000000009")
        );
        subscriberRepository.saveAll(subscribers);

        generateCdrRecords(subscribers);
    }

    private void generateCdrRecords(List<Subscriber> subscribers) {
        Random random = new Random();

        int randomSeconds = random.nextInt(60);
        int randomMinutes = random.nextInt(10);

        LocalDateTime startOfYear = LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, randomMinutes, randomSeconds);

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

            currentTime = callEnd.plusMinutes(random.nextInt(30));
        }
    }
}
