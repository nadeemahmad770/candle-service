package com.multibank.candle.exception;

import com.multibank.candle.model.dto.HistoryResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidIntervalException.class)
    public ResponseEntity<HistoryResponse> handleInvalidInterval(InvalidIntervalException ex) {
        log.warn("Invalid interval requested: {}", ex.getRequested());
        return ResponseEntity.badRequest().body(HistoryResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidRangeException.class)
    public ResponseEntity<HistoryResponse> handleInvalidRange(InvalidRangeException ex) {
        log.warn("Invalid range: from={} to={}", ex.getFrom(), ex.getTo());
        return ResponseEntity.badRequest().body(HistoryResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HistoryResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                       .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                       .reduce("", (a, b) -> a.isEmpty() ? b : a + "; " + b);
        log.warn("Validation failure: {}", msg);
        return ResponseEntity.badRequest().body(HistoryResponse.error(msg));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<HistoryResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Missing required parameter: " + ex.getParameterName();
        log.warn(msg);
        return ResponseEntity.badRequest().body(HistoryResponse.error(msg));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<HistoryResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Invalid value for parameter '" + ex.getName() + "': " + ex.getValue();
        log.warn(msg);
        return ResponseEntity.badRequest().body(HistoryResponse.error(msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HistoryResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(HistoryResponse.error("Internal server error"));
    }
}
