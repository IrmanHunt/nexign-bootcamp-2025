package com.romanlotohin.nexign_bootcamp_2025.service.generator;

import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;

import java.util.List;

public interface CdrGenerator {
    void generateCdrRecords(List<Subscriber> subscribers, int year, int daysInterval);
}