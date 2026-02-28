package com.multibank.candle.model.domain;

import lombok.Getter;

@Getter
public class SimulatedSymbol {
    final String symbol;
    double currentPrice;

    public SimulatedSymbol(String symbol, double initialPrice) {
        this.symbol = symbol;
        this.currentPrice = initialPrice;
    }
}