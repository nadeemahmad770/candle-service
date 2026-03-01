package com.multibank.candle.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandleBuilderTest {

    @Test
    void testBuildWithSinglePrice() {
        long time = 1000;
        CandleBuilder builder = new CandleBuilder(time);

        builder.update(100.0);
        Candle candle = builder.build();

        assertEquals(time, candle.time());
        assertEquals(100.0, candle.open());
        assertEquals(100.0, candle.high());
        assertEquals(100.0, candle.low());
        assertEquals(100.0, candle.close());
        assertEquals(1, candle.volume());
    }

    @Test
    void testBuildWithMultiplePrices() {
        long time = 1000;
        CandleBuilder builder = new CandleBuilder(time);

        builder.update(100.0);  // open
        builder.update(105.0);  // new high
        builder.update(102.0);  // between
        builder.update(103.0);  // close
        builder.update(99.0);   // new low

        Candle candle = builder.build();

        assertEquals(100.0, candle.open());
        assertEquals(105.0, candle.high());
        assertEquals(99.0, candle.low());
        assertEquals(99.0, candle.close());
        assertEquals(5, candle.volume());
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        CandleBuilder builder = new CandleBuilder(1000);
        int threadCount = 10;
        int updatesPerThread = 100;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < updatesPerThread; j++) {
                    double price = 100.0 + (threadId * 10) + j;
                    builder.update(price);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Candle candle = builder.build();
        assertEquals(threadCount * updatesPerThread, candle.volume());
    }
}
