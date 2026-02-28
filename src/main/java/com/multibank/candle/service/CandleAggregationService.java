package com.multibank.candle.service;

import com.multibank.candle.constants.AppConstants;
import com.multibank.candle.enums.Interval;
import com.multibank.candle.model.domain.BidAskEvent;
import com.multibank.candle.model.domain.Candle;
import com.multibank.candle.model.domain.CandleKey;
import com.multibank.candle.model.domain.MutableCandleAccumulator;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CandleAggregationService {

    private static final Logger log = LoggerFactory.getLogger(CandleAggregationService.class);

    private final List<Interval> intervals;
    private final CandleStorageService storageService;
    private final long graceSeconds;
    private final ConcurrentMap<CandleKey, MutableCandleAccumulator> openWindows = new ConcurrentHashMap<>();
    private final Counter ticksReceived;
    private final Counter candlesFlushed;
    private final Timer aggregationTimer;

    public CandleAggregationService(List<Interval> intervals, CandleStorageService storageService,
                                    long graceSeconds, MeterRegistry meterRegistry) {

        this.intervals = intervals;
        this.storageService = storageService;
        this.graceSeconds = graceSeconds;
        this.ticksReceived = meterRegistry.counter(AppConstants.METRIC_TICKS_RECEIVED);
        this.candlesFlushed = meterRegistry.counter(AppConstants.METRIC_CANDLES_FLUSHED);
        this.aggregationTimer = meterRegistry.timer(AppConstants.METRIC_AGGREGATION_TIMER);
    }

    public void ingest(BidAskEvent event) {
        aggregationTimer.record(() -> doIngest(event));
        ticksReceived.increment();
    }

    private void doIngest(BidAskEvent event) {
        double midPrice = event.midPrice();
        long tsSeconds = event.timestampSeconds();

        log.debug("Tick  symbol={}  mid={}  ts={}", event.symbol(), midPrice, tsSeconds);

        for (Interval interval : intervals) {
            long openTime = interval.alignToWindow(tsSeconds);
            CandleKey key = new CandleKey(event.symbol(), interval, openTime);

            openWindows.computeIfAbsent(key, MutableCandleAccumulator::new).apply(midPrice);
        }
    }

    @Scheduled(fixedRate = AppConstants.FLUSH_INTERVAL_MS)
    public void flushClosedWindows() {
        long nowSeconds = System.currentTimeMillis() / 1_000L;
        List<CandleKey> toRemove = new ArrayList<>();

        for (var entry : openWindows.entrySet()) {
            CandleKey key = entry.getKey();
            MutableCandleAccumulator acc = entry.getValue();

            long windowEnd = key.openTime() + key.interval().getSeconds();
            boolean windowClosed = nowSeconds >= windowEnd + graceSeconds;

            if (windowClosed && acc.hasData()) {
                Candle candle = acc.snapshot();
                if (candle != null) {
                    storageService.save(key.symbol(), key.interval(), candle);
                    candlesFlushed.increment();
                    log.info("Flushed  key={}  o={} h={} l={} c={} v={}", key, candle.open(), candle.high(), candle.low(), candle.close(), candle.volume());
                }
                toRemove.add(key);
            }
        }

        toRemove.forEach(openWindows::remove);
    }

    public void flushAll() {
        log.info("Shutdown flush: {} open windows", openWindows.size());
        openWindows.forEach((key, acc) -> {
            if (acc.hasData()) {
                Candle candle = acc.snapshot();
                if (candle != null) {
                    storageService.save(key.symbol(), key.interval(), candle);
                    log.info("Shutdown flush  key={}", key);
                }
            }
        });
        openWindows.clear();
    }

    public int openWindowCount() {
        return openWindows.size();
    }
}
