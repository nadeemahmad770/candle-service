package com.multibank.candle.exception;

import lombok.Getter;

@Getter
public class InvalidRangeException extends RuntimeException {

    private final long from;
    private final long to;

    public InvalidRangeException(long from, long to) {
        super("Invalid time range: from (" + from + ") must be less than to (" + to + ")");
        this.from = from;
        this.to   = to;
    }

}

