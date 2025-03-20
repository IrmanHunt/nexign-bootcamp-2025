package com.romanlotohin.nexign_bootcamp_2025;

import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import com.romanlotohin.nexign_bootcamp_2025.service.CdrGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final SubscriberRepository subscriberRepository;
    private final CdrGenerator cdrGenerator;

    public DataInitializer(SubscriberRepository subscriberRepository, CdrGenerator cdrGenerator) {
        this.subscriberRepository = subscriberRepository;
        this.cdrGenerator = cdrGenerator;
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

        cdrGenerator.generateCdrRecords(subscribers);
    }
}
