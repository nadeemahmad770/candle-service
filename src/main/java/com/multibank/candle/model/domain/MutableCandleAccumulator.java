package com.multibank.candle.model.domain;

public class MutableCandleAccumulator {
    private final CandleBuilder candleBuilder;
    private volatile boolean hasData = false;

    public MutableCandleAccumulator(CandleKey key) {
        this.candleBuilder = new CandleBuilder(key.openTime());
    }

    public synchronized void apply(double price) {
        candleBuilder.update(price);
        hasData = true;
    }

    public boolean hasData() {
        return hasData;
    }

    public synchronized Candle snapshot() {
        return candleBuilder.build();
    }
}

