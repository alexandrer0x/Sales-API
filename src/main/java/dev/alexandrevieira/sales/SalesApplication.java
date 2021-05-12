package dev.alexandrevieira.sales;

import dev.alexandrevieira.sales.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SalesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SalesApplication.class, args);
    }

    @Autowired
    private JwtService jwtService;

    @Override
    public void run(String... args) throws Exception {

    }
}
