package dev.alexandrevieira.sales.services;

import dev.alexandrevieira.sales.domain.entities.Product;
import dev.alexandrevieira.sales.domain.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService extends GenericEntityService<Product, Long, ProductRepository> {
    public ProductService(ProductRepository productRepository) {
        super(productRepository);
    }

    public Product save(Product product) {
        log.info(this.getClass().getSimpleName() + ".save(Product product)");
        product.setId(null);
        return repository.save(product);
    }
}
