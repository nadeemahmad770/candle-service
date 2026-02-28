package com.multibank.candle.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record HistoryRequest(
        @NotBlank(message = "symbol must not be blank")   String symbol,
        @NotBlank(message = "interval must not be blank") String interval,
        @Positive(message = "from must be a positive UNIX timestamp") long from,
        @Positive(message = "to must be a positive UNIX timestamp")   long to
) {}
