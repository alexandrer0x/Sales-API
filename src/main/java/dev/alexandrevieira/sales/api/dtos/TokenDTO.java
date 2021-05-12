package dev.alexandrevieira.sales.api.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
//DTO class to respond token authentication
public class TokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(position = 1)
    private String username;

    @ApiModelProperty(position = 2)
    private String token;

    public TokenDTO(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
