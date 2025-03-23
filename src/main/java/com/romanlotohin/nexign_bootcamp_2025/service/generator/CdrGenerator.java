package com.romanlotohin.nexign_bootcamp_2025.service.generator;

import com.romanlotohin.nexign_bootcamp_2025.entity.Subscriber;

import java.util.List;

/**
 * Интерфейс для генерации записей о звонках CDR
 */
public interface CdrGenerator {

    /**
     * Генерирует записи о звонках для списка абонентов за заданный год и интервал дней
     *
     * @param subscribers список абонентов, для которых будут генерироваться записи о звонках
     * @param year год, для которого генерируются записи
     * @param daysInterval интервал в днях, который будет использован для генерации данных
     */
    void generateCdrRecords(List<Subscriber> subscribers, int year, int daysInterval);
}