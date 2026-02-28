package com.multibank.candle.model.domain;

public class CandleBuilder {

    private final long time;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private boolean initialized = false;

    public CandleBuilder(long time) {
        this.time = time;
    }

    public synchronized void update(double price) {
        if (!initialized) {
            open = high = low = close = price;
            initialized = true;
        } else {
            high = Math.max(high, price);
            low = Math.min(low, price);
            close = price;
        }
        volume++;
    }

    public Candle build() {
        return new Candle(time, open, high, low, close, volume);
    }
}