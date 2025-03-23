package com.romanlotohin.nexign_bootcamp_2025.service.generator;

import com.romanlotohin.nexign_bootcamp_2025.entity.CdrRecord;
import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;
import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ComplexCdrGeneratorTest {

    @Mock
    private CdrRecordRepository cdrRecordRepository;

    @InjectMocks
    private ComplexCdrGenerator complexCdrGenerator;

    @Test
    public void testGenerateCdrRecords_SingleIteration() {
        Subscriber subscriber1 = new Subscriber("1111111111");
        Subscriber subscriber2 = new Subscriber("2222222222");
        List<Subscriber> subscribers = Arrays.asList(subscriber1, subscriber2);

        int generationYear = 2025;
        int daysInterval = 400;

        complexCdrGenerator.generateCdrRecords(subscribers, generationYear, daysInterval);

        verify(cdrRecordRepository, times(1)).save(any(CdrRecord.class));
    }

    @Test
    public void testGenerateCdrRecords_RecordIntegrity() {
        Subscriber subscriber1 = new Subscriber("1111111111");
        Subscriber subscriber2 = new Subscriber("2222222222");
        List<Subscriber> subscribers = Arrays.asList(subscriber1, subscriber2);

        int generationYear = 2025;
        int daysInterval = 400;

        complexCdrGenerator.generateCdrRecords(subscribers, generationYear, daysInterval);

        ArgumentCaptor<CdrRecord> captor = ArgumentCaptor.forClass(CdrRecord.class);
        verify(cdrRecordRepository, times(1)).save(captor.capture());
        CdrRecord savedRecord = captor.getValue();

        assertTrue(savedRecord.getCallStart().isBefore(savedRecord.getCallEnd()),
                "Время начала вызова должно быть раньше времени окончания");

        assertNotEquals(savedRecord.getCallerMsisdn(), savedRecord.getCalleeMsisdn(),
                "Был передан один и тот же номер");
    }
}
