package com.multibank.candle.constants;

public final class AppConstants {

    private AppConstants() {

    }

    public static final long FLUSH_INTERVAL_MS = 1_000L;
    public static final long SIMULATOR_EMIT_INTERVAL_MS = 100L;
    public static final double SPREAD_FRACTION = 0.0001;
    public static final String METRIC_TICKS_RECEIVED    = "candle.ticks.received";
    public static final String METRIC_CANDLES_FLUSHED   = "candle.candles.flushed";
    public static final String METRIC_AGGREGATION_TIMER = "candle.aggregation.duration";
    public static final String STATUS_OK      = "ok";
    public static final String STATUS_NO_DATA = "no_data";
    public static final String STATUS_ERROR   = "error";
    public static final int DB_SEQ_ALLOCATION_SIZE = 50;
    public static final int DB_SYMBOL_MAX_LENGTH = 20;
    public static final int DB_INTERVAL_CODE_MAX_LENGTH = 5;
}
