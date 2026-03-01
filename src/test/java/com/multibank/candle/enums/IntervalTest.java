package com.multibank.candle.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class IntervalTest {

    @Test
    void testIntervalCodes() {
        assertEquals("1s", Interval.S1.code());
        assertEquals("5s", Interval.S5.code());
        assertEquals("1m", Interval.M1.code());
        assertEquals("15m", Interval.M15.code());
        assertEquals("1h", Interval.H1.code());
    }

    @Test
    void testIntervalSeconds() {
        assertEquals(1, Interval.S1.seconds());
        assertEquals(5, Interval.S5.seconds());
        assertEquals(60, Interval.M1.seconds());
        assertEquals(900, Interval.M15.seconds());
        assertEquals(3600, Interval.H1.seconds());
    }

    @Test
    void testFromCode() {
        assertEquals(Interval.S1, Interval.fromCode("1s"));
        assertEquals(Interval.M1, Interval.fromCode("1m"));
        assertEquals(Interval.H1, Interval.fromCode("1h"));
    }

    @Test
    void testFromCodeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Interval.fromCode("99m"));
        assertThrows(IllegalArgumentException.class, () -> Interval.fromCode("invalid"));
    }

    @Test
    void testAlignToWindow() {
        long timestamp = 1234567;
        long aligned = Interval.M1.alignToWindow(timestamp);
        assertEquals(1234560, aligned);

        assertEquals(0, aligned % Interval.M1.seconds());
    }

    @Test
    void testAlignToWindowMultipleIntervals() {
        long timestamp = 3661; // 1 hour, 1 minute, 1 second

        assertEquals(timestamp, Interval.S1.alignToWindow(3661));

        assertEquals(3660, Interval.M1.alignToWindow(3661));

        assertEquals(3600, Interval.H1.alignToWindow(3661));
    }

    @Test
    void testAlignmentMethodsEquivalent() {
        long timestamp = 1234567;
        assertEquals(
                Interval.M1.alignToCandleOpen(timestamp),
                Interval.M1.alignToWindow(timestamp)
        );
    }
}
