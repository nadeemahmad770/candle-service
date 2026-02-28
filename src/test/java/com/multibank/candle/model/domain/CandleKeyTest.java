package com.multibank.candle.model.domain;

import com.multibank.candle.enums.Interval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CandleKey Tests")
class CandleKeyTest {

    @Test
    @DisplayName("Should create valid CandleKey with all valid parameters")
    void testCreateValidCandleKey() {
        String symbol = "BTC-USD";
        Interval interval = Interval.M1;
        long openTime = 1620000000L;

        CandleKey key = new CandleKey(symbol, interval, openTime);

        assertNotNull(key);
        assertEquals(symbol, key.symbol());
        assertEquals(interval, key.interval());
        assertEquals(openTime, key.openTime());
    }

    @Test
    @DisplayName("Should create valid CandleKey with different symbols")
    void testCreateCandleKeyWithDifferentSymbols() {
        String[] symbols = {"BTC-USD", "ETH-USD", "SOL-USD", "XRP-USD", "ADA-USD"};

        for (String symbol : symbols) {
            CandleKey key = new CandleKey(symbol, Interval.M1, 1620000000L);
            assertEquals(symbol, key.symbol());
        }
    }

    @Test
    @DisplayName("Should create valid CandleKey with different intervals")
    void testCreateCandleKeyWithDifferentIntervals() {
        Interval[] intervals = {Interval.S1, Interval.S5, Interval.M1, Interval.M15, Interval.H1};

        for (Interval interval : intervals) {
            CandleKey key = new CandleKey("BTC-USD", interval, 1620000000L);
            assertEquals(interval, key.interval());
        }
    }

    @Test
    @DisplayName("Should create valid CandleKey with zero openTime")
    void testCreateCandleKeyWithZeroOpenTime() {
        CandleKey key = new CandleKey("BTC-USD", Interval.M1, 0L);
        assertEquals(0L, key.openTime());
    }

    @Test
    @DisplayName("Should create valid CandleKey with large openTime")
    void testCreateCandleKeyWithLargeOpenTime() {
        long largeTime = Long.MAX_VALUE;
        CandleKey key = new CandleKey("BTC-USD", Interval.M1, largeTime);
        assertEquals(largeTime, key.openTime());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when symbol is null")
    void testSymbolCannotBeNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey(null, Interval.M1, 1620000000L)
        );

        assertEquals("symbol must not be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when symbol is empty string")
    void testSymbolCannotBeEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("", Interval.M1, 1620000000L)
        );

        assertEquals("symbol must not be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when symbol is whitespace only")
    void testSymbolCannotBeWhitespaceOnly() {
        String[] blankSymbols = {"   ", "\t", "\n", " \t \n "};

        for (String blankSymbol : blankSymbols) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new CandleKey(blankSymbol, Interval.M1, 1620000000L),
                    "Should reject symbol: '" + blankSymbol + "'"
            );

            assertEquals("symbol must not be blank", exception.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "BTC", "BTC-USD", "ETHEREUM-USDT", "test-symbol-123"})
    @DisplayName("Should accept various valid symbol formats")
    void testAcceptValidSymbolFormats(String symbol) {
        CandleKey key = new CandleKey(symbol, Interval.M1, 1620000000L);
        assertEquals(symbol, key.symbol());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when interval is null")
    void testIntervalCannotBeNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("BTC-USD", null, 1620000000L)
        );

        assertEquals("interval must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should accept all valid interval values")
    void testAcceptAllValidIntervals() {
        Interval[] allIntervals = Interval.values();

        for (Interval interval : allIntervals) {
            assertDoesNotThrow(() -> new CandleKey("BTC-USD", interval, 1620000000L));
        }
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when openTime is negative")
    void testOpenTimeCannotBeNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("BTC-USD", Interval.M1, -1L)
        );

        assertEquals("openTime must be non-negative", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L, -1000L, Long.MIN_VALUE})
    @DisplayName("Should reject various negative openTime values")
    void testRejectNegativeOpenTimes(long negativeTime) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("BTC-USD", Interval.M1, negativeTime)
        );

        assertEquals("openTime must be non-negative", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 1L, 1620000000L, 1700000000L, Long.MAX_VALUE})
    @DisplayName("Should accept non-negative openTime values")
    void testAcceptNonNegativeOpenTimes(long validTime) {
        CandleKey key = new CandleKey("BTC-USD", Interval.M1, validTime);
        assertEquals(validTime, key.openTime());
    }

    @Test
    @DisplayName("Should provide correct accessor for symbol")
    void testSymbolAccessor() {
        String expectedSymbol = "ETH-USD";
        CandleKey key = new CandleKey(expectedSymbol, Interval.M15, 1620000000L);

        assertEquals(expectedSymbol, key.symbol());
    }

    @Test
    @DisplayName("Should provide correct accessor for interval")
    void testIntervalAccessor() {
        Interval expectedInterval = Interval.H1;
        CandleKey key = new CandleKey("BTC-USD", expectedInterval, 1620000000L);

        assertEquals(expectedInterval, key.interval());
    }

    @Test
    @DisplayName("Should provide correct accessor for openTime")
    void testOpenTimeAccessor() {
        long expectedTime = 1620000000L;
        CandleKey key = new CandleKey("BTC-USD", Interval.M1, expectedTime);

        assertEquals(expectedTime, key.openTime());
    }

    @Test
    @DisplayName("Should be equal when all fields are the same")
    void testEqualityWithSameFields() {
        CandleKey key1 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        CandleKey key2 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);

        assertEquals(key1, key2);
    }

    @Test
    @DisplayName("Should not be equal when symbol differs")
    void testInequalityWithDifferentSymbol() {
        CandleKey key1 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        CandleKey key2 = new CandleKey("ETH-USD", Interval.M1, 1620000000L);

        assertNotEquals(key1, key2);
    }

    @Test
    @DisplayName("Should not be equal when interval differs")
    void testInequalityWithDifferentInterval() {
        CandleKey key1 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        CandleKey key2 = new CandleKey("BTC-USD", Interval.M15, 1620000000L);

        assertNotEquals(key1, key2);
    }

    @Test
    @DisplayName("Should not be equal when openTime differs")
    void testInequalityWithDifferentOpenTime() {
        CandleKey key1 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        CandleKey key2 = new CandleKey("BTC-USD", Interval.M1, 1620000060L);

        assertNotEquals(key1, key2);
    }

    @Test
    @DisplayName("Should have consistent hash code for equal objects")
    void testHashCodeConsistency() {
        CandleKey key1 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        CandleKey key2 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);

        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    @DisplayName("Should have different hash codes for different objects")
    void testHashCodeDifference() {
        CandleKey key1 = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        CandleKey key2 = new CandleKey("ETH-USD", Interval.M1, 1620000000L);

        assertNotEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    @DisplayName("Should provide meaningful toString representation")
    void testToStringRepresentation() {
        CandleKey key = new CandleKey("BTC-USD", Interval.M1, 1620000000L);
        String toString = key.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("BTC-USD"));
        assertTrue(toString.contains("M1"));
        assertTrue(toString.contains("1620000000"));
    }

    @Test
    @DisplayName("Should validate symbol first when multiple parameters are invalid")
    void testSymbolValidationTakePrecedence() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("", null, -1L)
        );

        assertEquals("symbol must not be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate interval when symbol is valid but interval is null")
    void testIntervalValidationAfterSymbol() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("BTC-USD", null, -1L)
        );

        assertEquals("interval must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should validate openTime when symbol and interval are valid")
    void testOpenTimeValidationLast() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CandleKey("BTC-USD", Interval.M1, -1L)
        );

        assertEquals("openTime must be non-negative", exception.getMessage());
    }
}
