package com.multibank.candle.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.multibank.candle.constants.AppConstants;
import com.multibank.candle.model.domain.Candle;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HistoryResponse(
        String s,
        String error,
        long[] t,
        double[] o,
        double[] h,
        double[] l,
        double[] c,
        long[] v
) {

    public static HistoryResponse of(List<Candle> candles) {
        return new HistoryResponse(
                AppConstants.STATUS_OK,
                null,
                candles.stream().mapToLong(Candle::time).toArray(),
                candles.stream().mapToDouble(Candle::open).toArray(),
                candles.stream().mapToDouble(Candle::high).toArray(),
                candles.stream().mapToDouble(Candle::low).toArray(),
                candles.stream().mapToDouble(Candle::close).toArray(),
                candles.stream().mapToLong(Candle::volume).toArray()
        );
    }

    public static HistoryResponse noData() {
        return new HistoryResponse(
                AppConstants.STATUS_NO_DATA, null, null, null, null, null, null, null);
    }

    public static HistoryResponse error(String message) {
        return new HistoryResponse(
                AppConstants.STATUS_ERROR, message, null, null, null, null, null, null);
    }
}
