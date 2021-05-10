package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.CredentialsDTO;
import dev.alexandrevieira.sales.api.dtos.TokenDTO;
import dev.alexandrevieira.sales.api.dtos.UserDTO;
import dev.alexandrevieira.sales.domain.entities.User;
import dev.alexandrevieira.sales.exceptions.InvalidCredentialsException;
import dev.alexandrevieira.sales.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashSet;

@RestController
@RequestMapping(path = "users")
public class UserResource {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO save(@RequestBody @Valid UserDTO userDTO) {
        String encryptedPassword = encoder.encode(userDTO.getPassword());
        userDTO.setPassword(encryptedPassword);
        User user = userDTO.toEntity();
        return userService.save(user).toDTO();
    }

    @PostMapping(path = "auth")
    public TokenDTO authenticate(@RequestBody CredentialsDTO dto) {
        try {
            User user = new User(null, dto.getUsername(), dto.getPassword(), new HashSet<>());

            String token = userService.authenticate(user);

            return new TokenDTO(user.getUsername(), token);

        }
        catch (UsernameNotFoundException | InvalidCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    @GetMapping(path = "{id}")
    public UserDTO find(@PathVariable Long id) {
        return userService.find(id).toDTO();
    }
}