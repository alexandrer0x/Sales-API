package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.CredentialsDTO;
import dev.alexandrevieira.sales.api.dtos.TokenDTO;
import dev.alexandrevieira.sales.api.dtos.UserRequestDTO;
import dev.alexandrevieira.sales.api.dtos.UserResponseDTO;
import dev.alexandrevieira.sales.api.exception.ApiError;
import dev.alexandrevieira.sales.domain.entities.User;
import dev.alexandrevieira.sales.services.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

//Controller to the '/users' resource
@RestController
@RequestMapping(path = "users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserResource {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a user")
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Created",
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "location",
                                    description = "The URI to the created user",
                                    response = URI.class
                            )
                    }
            ),
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    public UserResponseDTO save(@RequestBody @Valid
                                @ApiParam(value = "User information", name = "user", required = true)
                                        UserRequestDTO userRequestDTO) {
        log.info(this.getClass().getSimpleName() + ".save(UserRequestDTO userRequestDTO)");
        String encryptedPassword = encoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encryptedPassword);
        User user = userRequestDTO.toEntity();
        return userService.save(user).toDTO();
    }

    @PostMapping(path = "login")
    @ResponseStatus(OK)
    @ApiOperation("User authentication with JWT")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)
    })
    public TokenDTO authenticate(@RequestBody @ApiParam(value = "User credentials",
            name = "credentials", required = true) CredentialsDTO dto) {

        log.info(this.getClass().getSimpleName() + ".authenticate(CredentialsDTO dto)");
        User user = new User(null, dto.getUsername(), dto.getPassword(), new HashSet<>());
        String token = userService.authenticate(user);
        return new TokenDTO(user.getUsername(), token);
    }

    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    @ApiOperation("Search for a user information by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public UserResponseDTO find(@PathVariable @ApiParam(value = "User id to search for", required = true) Long id) {
        log.info(this.getClass().getSimpleName() + ".find(Long id)");
        return userService.find(id).toDTO();
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Delete a user by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class)
    })
    public void delete(@PathVariable @ApiParam(value = "User id to delete", required = true) Long id) {
        log.info(this.getClass().getSimpleName() + ".delete(Long id)");
        userService.delete(id);
    }
}