package com.multibank.candle;

import com.multibank.candle.config.TestConfig;
import com.multibank.candle.model.domain.BidAskEvent;
import com.multibank.candle.service.CandleAggregationService;
import com.multibank.candle.service.CandleStorageService;
import com.multibank.candle.service.InMemoryCandleStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestConfig.class)
class CandleServiceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CandleAggregationService aggregationService;

    @Autowired
    private CandleStorageService storageService;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void setUp() {
        if (storageService instanceof InMemoryCandleStorageService inMemoryService) {
            inMemoryService.clear();
        }
    }

    @Test
    public void testEndToEndCandleAggregation() throws Exception {
        long nowSeconds = System.currentTimeMillis() / 1_000L;
        long minuteStart = ((nowSeconds - 120) / 60) * 60;
        long minuteEnd = minuteStart + 59;

        aggregationService.ingest(new BidAskEvent("TEST-USD", 100.0, 100.2, minuteStart * 1_000L));
        aggregationService.ingest(new BidAskEvent("TEST-USD", 100.5, 100.7, (minuteStart + 10) * 1_000L));
        aggregationService.ingest(new BidAskEvent("TEST-USD", 101.0, 101.2, (minuteStart + 20) * 1_000L));
        aggregationService.ingest(new BidAskEvent("TEST-USD", 100.8, 101.0, (minuteStart + 50) * 1_000L));

        aggregationService.flushClosedWindows();

        mvc.perform(get("/api/v1/history")
                        .param("symbol", "TEST-USD")
                        .param("interval", "1m")
                        .param("from", String.valueOf(minuteStart))
                        .param("to", String.valueOf(minuteEnd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.s", equalTo("ok")))
                .andExpect(jsonPath("$.t[0]", equalTo((int) minuteStart)))
                .andExpect(jsonPath("$.o[0]", closeTo(100.1, 0.05)))
                .andExpect(jsonPath("$.h[0]", closeTo(101.1, 0.05)))
                .andExpect(jsonPath("$.l[0]", closeTo(100.1, 0.05)))
                .andExpect(jsonPath("$.c[0]", closeTo(100.9, 0.05)))
                .andExpect(jsonPath("$.v[0]", equalTo(4)));
    }

    @Test
    public void testHealthCheck() throws Exception {
        mvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("UP")));
    }

    @Test
    public void testStatusEndpoint() throws Exception {
        mvc.perform(get("/api/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("RUNNING")))
                .andExpect(jsonPath("$.openWindows", isA(Integer.class)));
    }

    @Test
    public void testInvalidIntervalError() throws Exception {
        mvc.perform(get("/api/v1/history")
                        .param("symbol", "BTC-USD")
                        .param("interval", "99m")  // Invalid interval
                        .param("from", "1000000000")
                        .param("to", "1000003600"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.s", equalTo("error")))
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    @Test
    public void testInvalidRangeError() throws Exception {
        mvc.perform(get("/api/v1/history")
                        .param("symbol", "BTC-USD")
                        .param("interval", "1m")
                        .param("from", "1000003600")
                        .param("to", "1000000000"))  // to < from
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.s", equalTo("error")))
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    @Test
    public void testNoDataResponse() throws Exception {
        mvc.perform(get("/api/v1/history")
                        .param("symbol", "NONEXISTENT-USD")
                        .param("interval", "1m")
                        .param("from", "1000000000")
                        .param("to", "1000003600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.s", equalTo("no_data")));
    }

    @Test
    public void testMissingParameterError() throws Exception {
        mvc.perform(get("/api/v1/history")
                        .param("symbol", "BTC-USD")
                        .param("from", "1000000000")
                        .param("to", "1000003600"))
                .andExpect(status().isBadRequest());
    }
}
