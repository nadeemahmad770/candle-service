package com.multibank.candle.config;

import com.multibank.candle.repository.CandleRepository;
import com.multibank.candle.service.CandleStorageService;
import com.multibank.candle.service.InMemoryCandleStorageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public CandleStorageService testCandleStorageService(CandleRepository repository) {
        return new InMemoryCandleStorageService(repository);
    }
}

