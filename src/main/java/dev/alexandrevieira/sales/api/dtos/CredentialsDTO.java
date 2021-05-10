package dev.alexandrevieira.sales.api.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CredentialsDTO {
    private String username;
    private String password;
}
