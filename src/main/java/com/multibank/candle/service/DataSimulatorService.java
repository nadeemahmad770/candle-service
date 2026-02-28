package com.multibank.candle.service;

import com.multibank.candle.constants.AppConstants;
import com.multibank.candle.model.domain.BidAskEvent;
import com.multibank.candle.model.domain.SimulatedSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class DataSimulatorService {

    private static final Logger log = LoggerFactory.getLogger(DataSimulatorService.class);

    private final CandleAggregationService aggregationService;

    private static final List<SimulatedSymbol> SYMBOLS = List.of(
            new SimulatedSymbol("BTC-USD", 29_500.0),
            new SimulatedSymbol("ETH-USD", 1_900.0),
            new SimulatedSymbol("SOL-USD", 110.0)
    );

    @Autowired
    public DataSimulatorService(CandleAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @Scheduled(fixedRate = AppConstants.SIMULATOR_EMIT_INTERVAL_MS)
    public void emitTicks() {
        long now = System.currentTimeMillis();
        Random rng = ThreadLocalRandom.current();
        int count = 0;

        for (SimulatedSymbol simulatedSymbol : SYMBOLS) {
            int ticksThisEmission = 1 + rng.nextInt(3);
            for (int i = 0; i < ticksThisEmission; i++) {
                double currentPrice = simulatedSymbol.getCurrentPrice();
                double priceMove = (rng.nextDouble() - 0.5) * currentPrice * 0.001;
                currentPrice += priceMove;
                currentPrice = Math.max(currentPrice, 0.01);

                double mid = currentPrice;
                double halfSpread = mid * AppConstants.SPREAD_FRACTION / 2.0;
                double bid = mid - halfSpread;
                double ask = mid + halfSpread;

                BidAskEvent event = new BidAskEvent(simulatedSymbol.getSymbol(), bid, ask, now);
                aggregationService.ingest(event);
                count++;
            }
        }

        log.debug("Emitted {} ticks from {} symbols", count, SYMBOLS.size());
    }
}

