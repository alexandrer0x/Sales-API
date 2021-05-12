package dev.alexandrevieira.sales.api.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Getter
//Default object to respond requests with errors
public class ApiError implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(position = 1)
    private Integer status;

    @ApiModelProperty(position = 2)
    private List<String> errors;

    @ApiModelProperty(position = 3)
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
