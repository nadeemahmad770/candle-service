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

    /** Builds a successful column response from a non-empty list of candles. */
    public static HistoryResponse of(List<Candle> candles) {
        int n = candles.size();
        long[] t = new long[n];
        double[] o = new double[n];
        double[] h = new double[n];
        double[] l = new double[n];
        double[] c = new double[n];
        long[] v = new long[n];

        for (int i = 0; i < n; i++) {
            Candle candle = candles.get(i);
            t[i] = candle.time();
            o[i] = candle.open();
            h[i] = candle.high();
            l[i] = candle.low();
            c[i] = candle.close();
            v[i] = candle.volume();
        }

        return new HistoryResponse(AppConstants.STATUS_OK, null, t, o, h, l, c, v);
    }

    /** Response when the requested range contains no candles. */
    public static HistoryResponse noData() {
        return new HistoryResponse(
                AppConstants.STATUS_NO_DATA, null, null, null, null, null, null, null);
    }

    /** Error response — {@code errmsg} carries the reason. */
    public static HistoryResponse error(String message) {
        return new HistoryResponse(
                AppConstants.STATUS_ERROR, message, null, null, null, null, null, null);
    }
}
