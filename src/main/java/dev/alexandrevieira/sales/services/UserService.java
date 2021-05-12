package dev.alexandrevieira.sales.services;

import dev.alexandrevieira.sales.domain.entities.User;
import dev.alexandrevieira.sales.domain.repositories.UserRepository;
import dev.alexandrevieira.sales.exceptions.BusinessRuleException;
import dev.alexandrevieira.sales.exceptions.InvalidCredentialsException;
import dev.alexandrevieira.sales.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserService extends GenericEntityService<User, Long, UserRepository> implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtService jwtService;

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    @Transactional
    public User save(User user) {
        log.info(this.getClass().getSimpleName() + ".save(User user)");
        user.setId(null);

        try {
            user = repository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("Username already in use");
        }

        return user;
    }

    public String authenticate(User user) throws ResponseStatusException {
        log.info(this.getClass().getSimpleName() + ".authenticate(User user)");

        //try to authenticate, if the username or password is invalid throws a exception and respond 401 status
        try {
            UserDetails userDetails = loadUserByUsername(user.getUsername());
            boolean passwordsMatch = encoder.matches(user.getPassword(), userDetails.getPassword());

            if (passwordsMatch) {
                String token = jwtService.generateToken(userDetails);
                return token;
            } else {
                throw new InvalidCredentialsException();
            }
        } catch (UsernameNotFoundException | InvalidCredentialsException ex) {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(this.getClass().getSimpleName() + ".loadUserByUsername(String username)");
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }
}
