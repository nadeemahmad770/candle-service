package com.multibank.candle.service;

import com.multibank.candle.enums.Interval;
import com.multibank.candle.exception.InvalidIntervalException;
import com.multibank.candle.exception.InvalidRangeException;
import com.multibank.candle.model.domain.Candle;
import com.multibank.candle.model.dto.HistoryRequest;
import com.multibank.candle.model.dto.HistoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CandleQueryService {

    private static final Logger log = LoggerFactory.getLogger(CandleQueryService.class);

    private final CandleStorageService storageService;

    @Autowired
    public CandleQueryService(CandleStorageService storageService) {
        this.storageService = storageService;
    }

    public HistoryResponse query(HistoryRequest request) {
        if (request.from() >= request.to()) {
            throw new InvalidRangeException(request.from(), request.to());
        }

        Interval interval;
        try {
            interval = Interval.fromCode(request.interval());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid interval requested: {}", request.interval());
            throw new InvalidIntervalException(
                    request.interval(),
                    java.util.Set.of("1s", "5s", "1m", "15m", "1h")
            );
        }

        log.debug("History query  symbol={}  interval={}  from={}  to={}",
                  request.symbol(), interval, request.from(), request.to());

        List<Candle> candles = storageService.findByRange(
                request.symbol(), interval, request.from(), request.to());

        if (candles.isEmpty()) {
            return HistoryResponse.noData();
        }

        return HistoryResponse.of(candles);
    }
}
