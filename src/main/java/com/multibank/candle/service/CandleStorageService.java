package com.multibank.candle.service;

import com.multibank.candle.enums.Interval;
import com.multibank.candle.model.domain.Candle;
import com.multibank.candle.model.entity.CandleEntity;
import com.multibank.candle.repository.CandleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CandleStorageService {

    private static final Logger log = LoggerFactory.getLogger(CandleStorageService.class);

    private final CandleRepository repository;

    @Autowired
    public CandleStorageService(CandleRepository repository) {
        this.repository = repository;
    }

    public void save(String symbol, Interval interval, Candle candle) {
        String code = interval.getCode();

        repository.findBySymbolAndIntervalCodeAndOpenTime(symbol, code, candle.time())
                  .ifPresentOrElse(
                      existing -> {
                          existing.updateOhlcv(
                                  candle.open(), candle.high(),
                                  candle.low(),  candle.close(), candle.volume());
                          log.debug("Updated  symbol={} interval={} time={}",
                                    symbol, code, candle.time());
                      },
                      () -> {
                          repository.save(new CandleEntity(
                                  symbol, code, candle.time(),
                                  candle.open(), candle.high(),
                                  candle.low(),  candle.close(), candle.volume()));
                          log.debug("Inserted  symbol={} interval={} time={}",
                                    symbol, code, candle.time());
                      }
                  );
    }

    @Transactional(readOnly = true)
    public List<Candle> findByRange(String symbol, Interval interval, long from, long to) {
        return repository.findByRange(symbol, interval.getCode(), from, to)
                         .stream()
                         .map(e -> new Candle(
                                 e.getOpenTime(), e.getOpen(), e.getHigh(),
                                 e.getLow(),      e.getClose(), e.getVolume()))
                         .toList();
    }
}
