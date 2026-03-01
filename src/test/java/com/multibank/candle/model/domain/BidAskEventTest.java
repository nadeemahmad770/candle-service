package com.multibank.candle.model.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidAskEventTest {

    @Test
    void testValidEventAndMidPrice() {
        BidAskEvent event = new BidAskEvent("BTC-USD", 29_500.0, 29_502.0, 1_700_000_000_000L);

        assertEquals("BTC-USD", event.symbol());
        assertEquals(29_500.0, event.bid());
        assertEquals(29_502.0, event.ask());
        assertEquals(29_501.0, event.midPrice());
    }

    @Test
    void testTimestampSeconds() {
        long milliseconds = 1_700_000_000_000L;
        long seconds = 1_700_000_000L;

        BidAskEvent event = new BidAskEvent("ETH-USD", 1_900.0, 1_901.0, milliseconds);
        assertEquals(seconds, event.timestampSeconds());
    }

    @Test
    void testBlankSymbolRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent("", 100.0, 101.0, 1_000_000_000_000L));
    }

    @Test
    void testNullSymbolRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent(null, 100.0, 101.0, 1_000_000_000_000L));
    }

    @Test
    void testNonPositiveBidRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent("BTC-USD", 0.0, 101.0, 1_000_000_000_000L));
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent("BTC-USD", -10.0, 101.0, 1_000_000_000_000L));
    }

    @Test
    void testAskLessThanBidRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent("BTC-USD", 100.0, 99.0, 1_000_000_000_000L));
    }

    @Test
    void testNonPositiveTimestampRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent("BTC-USD", 100.0, 101.0, 0L));
        assertThrows(IllegalArgumentException.class,
                () -> new BidAskEvent("BTC-USD", 100.0, 101.0, -1_000L));
    }
}
