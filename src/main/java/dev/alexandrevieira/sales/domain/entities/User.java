package dev.alexandrevieira.sales.domain.entities;

import dev.alexandrevieira.sales.api.dtos.UserDTO;
import dev.alexandrevieira.sales.domain.enums.Profile;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Slf4j
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;
    public static final String USER = "USER";
    public static final String ADMIN = "USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    @Email
    @Getter @Setter
    private String username;

    @Column(nullable = false)
    @Getter @Setter
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "profiles"}))
    private Set<String> profiles = new HashSet<>();

    public User() {
        this.addProfile(Profile.USER);
    }

    public User(Long id, String username, String password, Set<Profile> profiles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profiles = profiles.stream().map(x -> x.getDescription()).collect(Collectors.toSet());

        this.addProfile(Profile.USER);
    }

    public User(UserDTO dto) {
        this.id = dto.getId();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.addProfile(Profile.USER);
    }

    public UserDTO toDTO() {
        return new UserDTO(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getProfiles().stream().map(
                x -> new SimpleGrantedAuthority(x.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(Profile profile) {
        return this.getAuthorities().contains(new SimpleGrantedAuthority(profile.getDescription()));
    }

    public Set<Profile> getProfiles() {
        return this.profiles.stream().map(x -> Profile.byDescription(x)).collect(Collectors.toSet());
    }

    public void addProfile(Profile profile) {
        this.profiles.add(profile.getDescription());
    }
}
