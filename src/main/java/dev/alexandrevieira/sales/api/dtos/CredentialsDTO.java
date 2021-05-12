package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@ToString
//DTO class to receive credentials
public class CredentialsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Email(message = "Invalid username")
    @NotEmpty(message = "Username field is empty")
    @ApiModelProperty(position = 1)
    private String username;

    @NotEmpty(message = "Password field is empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(position = 2)
    private String password;
}
