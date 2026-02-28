package com.multibank.candle.model.domain;


public record BidAskEvent(
        String symbol,
        double bid,
        double ask,
        long timestamp
) {

    public double midPrice() {
        return (bid + ask) / 2.0;
    }

    public long timestampSeconds() {
        return timestamp / 1_000L;
    }

    public BidAskEvent {
        if (symbol    == null || symbol.isBlank()) throw new IllegalArgumentException("symbol must not be blank");
        if (bid       <= 0)                        throw new IllegalArgumentException("bid must be positive");
        if (ask       <= 0)                        throw new IllegalArgumentException("ask must be positive");
        if (ask       <  bid)                      throw new IllegalArgumentException("ask must be >= bid");
        if (timestamp <= 0)                        throw new IllegalArgumentException("timestamp must be positive");
    }
}
