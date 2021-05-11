package dev.alexandrevieira.sales.config;

import dev.alexandrevieira.sales.domain.enums.Profile;
import dev.alexandrevieira.sales.security.jwt.JwtAuthFilter;
import dev.alexandrevieira.sales.security.jwt.JwtService;
import dev.alexandrevieira.sales.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    private static final String[] PUBLIC_GET = {"/products/**"};
    private static final String[] PUBLIC_POST = {"/users/**"};
    private static final String[] ADMIN = {"/**"};
    private static final String[] USER_POST = {"/orders/**"};
    private static final String[] USER_GET = {"/orders/**", "/customers/**"};

    private static final String[] SWAGGER_MATCHERS = {"/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/swagger-ui.html", "/webjars/**"};


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                .antMatchers(HttpMethod.POST, PUBLIC_POST).permitAll()
                .antMatchers(HttpMethod.GET, USER_GET).hasAuthority(Profile.USER.name())
                .antMatchers(HttpMethod.POST, USER_POST).hasAuthority(Profile.USER.name())
                .antMatchers(ADMIN).hasAuthority(Profile.ADMIN.name())
                .anyRequest().denyAll();
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SWAGGER_MATCHERS);
    }
}
