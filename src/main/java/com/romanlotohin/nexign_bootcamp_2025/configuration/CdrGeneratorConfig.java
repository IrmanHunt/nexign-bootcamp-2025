package com.romanlotohin.nexign_bootcamp_2025.configuration;

import com.romanlotohin.nexign_bootcamp_2025.repository.CdrRecordRepository;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.CdrGenerator;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.EasyCdrGenerator;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.ComplexCdrGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для генератора записей о звонках CDR.
 * В зависимости от значения свойства приложения, передаваемого через `app.cdr-generation-method`,
 * будет выбран соответствующий метод генерации: простой или сложный.
 */
@Configuration
public class CdrGeneratorConfig {

    /**
     * Создает и возвращает бин CdrGenerator в зависимости от конфигурации
     *
     * @param cdrGenerationMethod метод генерации записей о звонках, заданный в настройках приложения
     * @param cdrRecordRepository репозиторий для сохранения записей о звонках
     * @return соответствующий CdrGenerator
     * @throws IllegalArgumentException если метод генерации не распознан
     */
    @Bean
    public CdrGenerator cdrGenerator(@Value("${app.cdr-generation-method}") String cdrGenerationMethod,
                                     CdrRecordRepository cdrRecordRepository) {
        if ("easy".equalsIgnoreCase(cdrGenerationMethod)) {
            return new EasyCdrGenerator(cdrRecordRepository);
        } else if ("complex".equalsIgnoreCase(cdrGenerationMethod)) {
            return new ComplexCdrGenerator(cdrRecordRepository);
        } else {
            throw new IllegalArgumentException("Неизвестный способ cdr генерации: " + cdrGenerationMethod);
        }
    }
}