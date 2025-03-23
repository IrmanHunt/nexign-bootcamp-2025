package com.romanlotohin.nexign_bootcamp_2025;

import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.SubscriberRepository;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.CdrGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private CdrGenerator cdrGenerator;

    private DataInitializer dataInitializer;
    private int generationYear = 2025;
    private int daysInterval = 0;

    @Test
    public void testRunInitializesSubscribersAndGeneratesCdrRecords() {
        dataInitializer = new DataInitializer(subscriberRepository, cdrGenerator, generationYear, daysInterval);
        dataInitializer.run();

        ArgumentCaptor<List<Subscriber>> captor = ArgumentCaptor.forClass(List.class);
        verify(subscriberRepository).saveAll(captor.capture());
        List<Subscriber> savedSubscribers = captor.getValue();

        assertEquals(15, savedSubscribers.size(), "Должно быть сохранено 15 абонентов");

        verify(cdrGenerator).generateCdrRecords(eq(savedSubscribers), eq(generationYear), eq(daysInterval));
    }
}
