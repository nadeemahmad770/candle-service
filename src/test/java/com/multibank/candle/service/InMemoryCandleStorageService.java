package com.multibank.candle.service;

import com.multibank.candle.enums.Interval;
import com.multibank.candle.model.domain.Candle;
import com.multibank.candle.repository.CandleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCandleStorageService extends CandleStorageService {

    private final Map<String, Map<Interval, List<Candle>>> store = new HashMap<>();

    public InMemoryCandleStorageService(CandleRepository repository) {
        super(repository);
    }

    @Override
    public void save(String symbol, Interval interval, Candle candle) {
        store.computeIfAbsent(symbol, k -> new HashMap<>())
             .computeIfAbsent(interval, k -> new ArrayList<>())
             .add(candle);
    }

    @Override
    public List<Candle> findByRange(String symbol, Interval interval, long from, long to) {
        return store.getOrDefault(symbol, Map.of())
                    .getOrDefault(interval, List.of())
                    .stream()
                    .filter(c -> c.time() >= from && c.time() <= to)
                    .toList();
    }

    public void clear() {
        store.clear();
    }
}