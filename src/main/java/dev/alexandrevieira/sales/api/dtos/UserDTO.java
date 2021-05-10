package dev.alexandrevieira.sales.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.alexandrevieira.sales.domain.entities.User;
import dev.alexandrevieira.sales.domain.enums.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Slf4j
public class UserDTO {
    private Long id;

    @Email(message = "Invalid email")
    @NotEmpty(message = "Username field is empty")
    private String username;

    @NotEmpty(message = "Password field is empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Set<String> profiles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.profiles = user.getProfiles().stream().map(x -> x.name()).collect(Collectors.toSet());
    }

    public Set<Profile> getProfiles() {
        return this.profiles.stream().map(x -> Profile.byDescription(x)).collect(Collectors.toSet());
    }

    public void addProfile(Profile profile) {
        this.profiles.add(profile.name());
    }


    public User toEntity() {
        return new User(this);
    }
}
