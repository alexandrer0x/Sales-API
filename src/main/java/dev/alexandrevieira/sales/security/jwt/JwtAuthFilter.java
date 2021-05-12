package dev.alexandrevieira.sales.security.jwt;

import dev.alexandrevieira.sales.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug(this.getClass().getSimpleName() + ".doFilterInternal(HttpServletRequest request, " +
                "HttpServletResponse response, " +
                "FilterChain filterChain)");

        String prefix = "Bearer ";
        String authorization = request.getHeader("Authorization");

        //if exists the header 'Authorization' and starts with 'prefix'
        if (authorization != null && authorization.startsWith(prefix)) {
            String token = authorization.substring(7);
            boolean isValid = jwtService.tokenIsValid(token);

            if (isValid) {
                String username = jwtService.getUserUsername(token);
                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = getAuth(userDetails);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuth(UserDetails userDetails) {
        log.debug(this.getClass().getSimpleName() + ".getAuth(UserDetails userDetails)");
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
