package com.multibank.candle.repository;

import com.multibank.candle.model.entity.CandleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandleRepository extends JpaRepository<CandleEntity, Long> {

    /**
     * Fetches all candles for a symbol/interval within the inclusive time range
     * [{@code from}, {@code to}], ordered by openTime.
     */
    @Query("""
           SELECT c FROM CandleEntity c
           WHERE  c.symbol       = :symbol
             AND  c.intervalCode = :intervalCode
             AND  c.openTime    >= :from
             AND  c.openTime    <= :to
           ORDER BY c.openTime ASC
           """)
    List<CandleEntity> findByRange(
            @Param("symbol")       String symbol,
            @Param("intervalCode") String intervalCode,
            @Param("from")         long   from,
            @Param("to")           long   to);


    Optional<CandleEntity> findBySymbolAndIntervalCodeAndOpenTime(
            String symbol, String intervalCode, long openTime);
}
