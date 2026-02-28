package com.multibank.candle.controller;

import com.multibank.candle.model.dto.HistoryRequest;
import com.multibank.candle.model.dto.HistoryResponse;
import com.multibank.candle.service.CandleQueryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1")
public class HistoryController {

    private final CandleQueryService candleQueryService;

    @Autowired
    public HistoryController(CandleQueryService candleQueryService) {
        this.candleQueryService = candleQueryService;
    }

    @GetMapping("/history")
    public ResponseEntity<HistoryResponse> history(
            @RequestParam @NotBlank String symbol,
            @RequestParam @NotBlank String interval,
            @RequestParam @Positive long   from,
            @RequestParam @Positive long   to) {

        HistoryRequest request = new HistoryRequest(symbol, interval, from, to);
        return ResponseEntity.ok(candleQueryService.query(request));
    }
}
