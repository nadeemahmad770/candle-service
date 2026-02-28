package com.multibank.candle.model.domain;

import com.multibank.candle.enums.Interval;


public record CandleKey(
        String symbol,
        Interval interval,
        long openTime
) {

    public CandleKey {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("symbol must not be blank");
        }
        if (interval == null) {
            throw new IllegalArgumentException("interval must not be null");
        }
        if (openTime < 0) {
            throw new IllegalArgumentException("openTime must be non-negative");
        }
    }
}

