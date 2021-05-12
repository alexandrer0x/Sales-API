package dev.alexandrevieira.sales.api.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenDTO {
    @ApiModelProperty(position = 1)
    private String username;

    @ApiModelProperty(position = 2)
    private String token;

    public TokenDTO(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
