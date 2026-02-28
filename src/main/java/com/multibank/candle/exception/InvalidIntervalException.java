package com.multibank.candle.exception;

import lombok.Getter;

import java.util.Set;


@Getter
public class InvalidIntervalException extends RuntimeException {

    private final String requested;
    private final Set<String> valid;

    public InvalidIntervalException(String requested, Set<String> valid) {
        super("Unknown interval: '" + requested + "'. Valid values: " + valid);
        this.requested = requested;
        this.valid = valid;
    }

}
