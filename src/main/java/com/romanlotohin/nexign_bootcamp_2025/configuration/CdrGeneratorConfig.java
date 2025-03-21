package com.romanlotohin.nexign_bootcamp_2025.configuration;

import com.romanlotohin.nexign_bootcamp_2025.service.generator.CdrGenerator;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.EasyCdrGenerator;
import com.romanlotohin.nexign_bootcamp_2025.service.generator.ComplexCdrGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdrGeneratorConfig {
    @Bean
    public CdrGenerator cdrGenerator(@Value("${app.cdr-generation-method}") String cdrGenerationMethod) {
        System.out.println(cdrGenerationMethod);
        if ("easy".equalsIgnoreCase(cdrGenerationMethod)) {
            return new EasyCdrGenerator();
        } else if ("complex".equalsIgnoreCase(cdrGenerationMethod)) {
            return new ComplexCdrGenerator();
        } else {
            throw new IllegalArgumentException("Неизвестный способ cdr генерации: " + cdrGenerationMethod);
        }
    }
}