package org.example.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    // Registra o módulo de datas Java 8 (OffsetDateTime, LocalDateTime etc.)
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    // Garante saída em ISO-8601 (sem timestamps numéricos)
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsr310Customizer() {
        return builder -> builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
