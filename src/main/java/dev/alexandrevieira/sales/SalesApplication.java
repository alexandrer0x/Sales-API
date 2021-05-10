package dev.alexandrevieira.sales;

import dev.alexandrevieira.sales.domain.repositories.CustomerRepository;
import dev.alexandrevieira.sales.domain.repositories.OrderRepository;
import dev.alexandrevieira.sales.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootApplication
public class SalesApplication implements CommandLineRunner {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Bean(name = "modelMapper")
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(SalesApplication.class, args);
    }

    @Autowired
    private JwtService jwtService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

    }
}
