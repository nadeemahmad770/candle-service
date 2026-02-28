package com.multibank.candle.config;

import com.multibank.candle.enums.Interval;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CandleServiceConfig {

    @Bean
    public List<Interval> intervals() {
        return List.of(
                Interval.S1,
                Interval.S5,
                Interval.M1,
                Interval.M15,
                Interval.H1
        );
    }

    @Bean
    public Long graceSeconds() {
        return 5L; // 5 seconds
    }
}

