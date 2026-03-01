package com.multibank.candle.config;

import com.multibank.candle.service.CandleAggregationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHandler {

    private static final Logger log = LoggerFactory.getLogger(ShutdownHandler.class);

    private final CandleAggregationService aggregationService;

    public ShutdownHandler(CandleAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @EventListener(ContextClosedEvent.class)
    public void onShutdown() {
        log.info("Application shutting down; flushing all pending candles...");
        aggregationService.flushAll();
        log.info("Shutdown flush complete.");
    }
}

