package com.multibank.candle.model.entity;

import com.multibank.candle.constants.AppConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "candle", uniqueConstraints = @UniqueConstraint(name = "uq_candle_symbol_interval_time",
        columnNames = {"symbol", "interval_code", "open_time"}),
        indexes = {@Index(name = "idx_candle_query", columnList = "symbol, interval_code, open_time")})
public class CandleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candle_seq")
    @SequenceGenerator(name = "candle_seq", sequenceName = "candle_seq")
    private Long id;

    @Column(name = "symbol", nullable = false, length = AppConstants.DB_SYMBOL_MAX_LENGTH)
    private String symbol;

    @Column(name = "interval_code", nullable = false, length = AppConstants.DB_INTERVAL_CODE_MAX_LENGTH)
    private String intervalCode;

    @Column(name = "open_time", nullable = false)
    private long openTime;

    @Column(nullable = false)
    private double open;
    @Column(nullable = false)
    private double high;
    @Column(nullable = false)
    private double low;
    @Column(nullable = false)
    private double close;
    @Column(nullable = false)
    private long volume;


    public CandleEntity(String symbol, String intervalCode, long openTime, double open, double high, double low, double close, long volume) {
        this.symbol = symbol;
        this.intervalCode = intervalCode;
        this.openTime = openTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public void updateOhlcv(double open, double high, double low, double close, long volume) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}
