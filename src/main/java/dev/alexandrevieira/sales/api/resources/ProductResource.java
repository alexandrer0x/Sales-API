package dev.alexandrevieira.sales.api.resources;

import dev.alexandrevieira.sales.api.dtos.ProductDTO;
import dev.alexandrevieira.sales.domain.entities.Product;
import dev.alexandrevieira.sales.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "products")
public class ProductResource {
    @Autowired
    private ProductService service;

    @GetMapping(path = "{id}")
    @ResponseStatus(OK)
    public ProductDTO find(@PathVariable Long id) {
        return service.find(id).toDTO();
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ProductDTO> filter(ProductDTO filter) {
        Product product = filter.toEntity();
        List<Product> list = service.filter(product);

        return list.stream().map(x -> x.toDTO()).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ProductDTO save(@RequestBody @Valid ProductDTO dto) {
        Product product = dto.toEntity();
        return service.save(product).toDTO();
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        Product product = dto.toEntity();
        service.update(id, product);
    }
}
