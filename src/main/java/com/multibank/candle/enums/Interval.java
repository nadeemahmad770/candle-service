package com.multibank.candle.enums;

import lombok.Getter;

import java.util.Map;

@Getter
public enum Interval {
    S1("1s", 1),
    S5("5s", 5),
    M1("1m", 60),
    M15("15m", 15 * 60),
    H1("1h", 60 * 60);

    private final String code;
    private final long seconds;

    Interval(String code, long seconds) {
        this.code = code;
        this.seconds = seconds;
    }

    public String code() { return code; }
    public long seconds() { return seconds; }

    private static final Map<String, Interval> BY_CODE =
            Map.of("1s", S1, "5s", S5, "1m", M1, "15m", M15, "1h", H1);

    public static Interval fromCode(String code) {
        var i = BY_CODE.get(code);
        if (i == null) throw new IllegalArgumentException("Unsupported interval: " + code);
        return i;
    }

    public long alignToCandleOpen(long epochSeconds) {
        return (epochSeconds / seconds) * seconds;
    }

    public long alignToWindow(long epochSeconds) {
        return (epochSeconds / seconds) * seconds;
    }
}