package dev.alexandrevieira.sales.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Getter
public class ApiError {
    private Integer status;
    private List<String> errors;
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String error) {
        this.status = status.value();
        this.errors = Collections.singletonList(error);
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, List<String> errors) {
        this.status = status.value();
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
