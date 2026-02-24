package com.company.student.app.exception;

import com.company.student.app.dto.HttpApiResponse;
import com.company.student.app.model.enums.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpApiResponse<Void>> handleEntityNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request,
            Locale locale) {

        String message = messageSource.getMessage(
                ex.getMessage(),   // entity.not.found
                null,
                locale
        );

        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .status(404)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("Validation error: {}", errors);

        HttpApiResponse<Map<String, String>> response =
                HttpApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed")
                        .data(errors)
                        .errorCode(ErrorCode.VALIDATION_ERROR)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpApiResponse<Void>> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Bad request: {}", ex.getMessage());

        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .errorCode(ErrorCode.BAD_REQUEST)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpApiResponse<Void>> handleGlobal(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred", ex);

        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message("Internal server error")
                .errorCode(ErrorCode.INTERNAL_ERROR)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<HttpApiResponse<Void>> handleAccessDenied(
            Exception ex,
            HttpServletRequest request) {

        log.warn("Access denied: {}", ex.getMessage());

        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message("Access denied")
                .errorCode(ErrorCode.ACCESS_DENIED)
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
