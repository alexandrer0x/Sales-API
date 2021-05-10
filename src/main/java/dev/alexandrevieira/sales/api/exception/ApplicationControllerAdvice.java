package dev.alexandrevieira.sales.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dev.alexandrevieira.sales.exceptions.BusinessRuleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRuleException(BusinessRuleException ex) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError error = new ApiError(status, ex.getMessage());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
        final HttpStatus status = ex.getStatus();
        ApiError error = new ApiError(status, status.getReasonPhrase());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> messages = ex.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError error = new ApiError(status, messages);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(InvalidFormatException ex) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError error = new ApiError(status, ex.getOriginalMessage());
        return ResponseEntity.status(status).body(error);
    }
}
