package com.multibank.candle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI candleServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Candle Aggregation Service API")
                        .description("""
                                Real-time OHLCV candlestick aggregation service for financial market data.
                                Ingests bid/ask events and exposes historical candle data in TradingView UDF format.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Candle Service Team")
                                .email("team@multibank.com"))
                        .license(new License()
                                .name("Multibank")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")
                ));
    }
}
