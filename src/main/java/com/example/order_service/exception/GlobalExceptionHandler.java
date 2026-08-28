package com.example.order_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.order_service.dto.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(
            OrderNotFoundException ex) {

        ApiError error = new ApiError();

        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("ORDER_NOT_FOUND");
        error.setMessage(ex.getMessage());
        error.setPath("/api/v1/orders");

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    String message = ex.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .findFirst()
	            .orElse("Invalid request");

	    ApiError error = new ApiError();

	    error.setTimestamp(LocalDateTime.now());
	    error.setStatus(HttpStatus.BAD_REQUEST.value());
	    error.setError("VALIDATION_ERROR");
	    error.setMessage(message);
	    error.setPath("/api/v1/orders");

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}
}