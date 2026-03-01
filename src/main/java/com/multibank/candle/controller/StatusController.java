package com.multibank.candle.controller;

import com.multibank.candle.service.CandleAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class StatusController {

    private final CandleAggregationService aggregationService;

    public StatusController(CandleAggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "openWindows", aggregationService.openWindowCount(),
                "status", "RUNNING"
        ));
    }
}

