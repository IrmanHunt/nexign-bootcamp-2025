package com.romanlotohin.nexign_bootcamp_2025.service.generator;

import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EasyCdrGeneratorTest {

    @Mock
    private CdrRecordRepository cdrRecordRepository;

    @InjectMocks
    private EasyCdrGenerator easyCdrGenerator;

    @Test
    public void testGenerateCdrRecords_SingleIteration() {
        Subscriber subscriber1 = new Subscriber("1111111111");
        Subscriber subscriber2 = new Subscriber("2222222222");
        List<Subscriber> subscribers = Arrays.asList(subscriber1, subscriber2);

        int generationYear = 2025;
        int daysInterval = 400;

        easyCdrGenerator.generateCdrRecords(subscribers, generationYear, daysInterval);

        verify(cdrRecordRepository, times(1)).save(any(CdrRecord.class));
    }
}
